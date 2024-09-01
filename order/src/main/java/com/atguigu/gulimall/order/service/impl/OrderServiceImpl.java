package com.atguigu.gulimall.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.atguigu.gulimall.common.exception.NoStockException;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.common.vo.MemberResponseVo;
import com.atguigu.gulimall.order.constant.OrderConstant;
import com.atguigu.gulimall.order.dao.OrderDao;
import com.atguigu.gulimall.order.entity.OrderEntity;
import com.atguigu.gulimall.order.entity.OrderItemEntity;
import com.atguigu.gulimall.order.enume.OrderStatusEnum;
import com.atguigu.gulimall.order.feign.CartFeignService;
import com.atguigu.gulimall.order.feign.MemberFeignService;
import com.atguigu.gulimall.order.feign.ProductFeignService;
import com.atguigu.gulimall.order.feign.WareFeignService;
import com.atguigu.gulimall.order.interceptor.LoginUserInterceptor;
import com.atguigu.gulimall.order.service.OrderItemService;
import com.atguigu.gulimall.order.service.OrderService;
import com.atguigu.gulimall.order.to.OrderCreateTo;
import com.atguigu.gulimall.order.to.SpuInfoVo;
import com.atguigu.gulimall.order.vo.FareVo;
import com.atguigu.gulimall.order.vo.MemberAddressVo;
import com.atguigu.gulimall.order.vo.OrderConfirmVo;
import com.atguigu.gulimall.order.vo.OrderItemVo;
import com.atguigu.gulimall.order.vo.OrderSubmitVo;
import com.atguigu.gulimall.order.vo.SkuStockVo;
import com.atguigu.gulimall.order.vo.SubmitOrderResponseVo;
import com.atguigu.gulimall.order.vo.WareSkuLockVo;

@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
    private ThreadLocal<OrderSubmitVo> orderConfirmVoLocal = new ThreadLocal<>();
    @Autowired
    private MemberFeignService memberFeignService;
    @Autowired
    private CartFeignService cartFeignService;
    @Autowired
    private ProductFeignService productFeignService;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    private WareFeignService wareFeignService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private OrderItemService orderItemService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>());

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVo confirmOrder() {

        OrderConfirmVo vo = new OrderConfirmVo();
        // 进行异步改造
        CompletableFuture<Void> addressFuture = CompletableFuture.runAsync(() -> {
            // 获得地址页
            LinkedHashMap<String, Object> user = (LinkedHashMap<String, Object>) LoginUserInterceptor.loginUser.get();
            if (user != null && !user.isEmpty()) {
                Long memberId = Long.valueOf((Integer) user.get("id"));
                if (memberId != null) {
                    List<MemberAddressVo> address = memberFeignService.getAddress(memberId);
                    if (address != null) {
                        vo.setMemberAddressVos(address);
                    } else {
                        vo.setMemberAddressVos(new ArrayList<>());
                    }
                } else {
                    vo.setMemberAddressVos(new ArrayList<>());
                }
            }

        }, threadPoolExecutor);
        CompletableFuture<Void> cartItemsFuture = CompletableFuture.supplyAsync(() -> {
            // 获得购物车
            List<OrderItemVo> currentCartItems = cartFeignService.getCurrentCartItems();
            if (currentCartItems != null && !currentCartItems.isEmpty()) {
                vo.setItems(currentCartItems);
            } else {
                vo.setItems(new ArrayList<>());
            }
            return currentCartItems.stream().map(item -> item.getSkuId()).toList();
        }, threadPoolExecutor).thenAcceptAsync((item) -> {
            R r = wareFeignService.getSkuHasStock(item);
            List<SkuStockVo> vos = (List<SkuStockVo>) r.getData(new TypeReference<List<SkuStockVo>>() {
            });
            if (vos != null && !vos.isEmpty()) {
                Map<Long, Boolean> stocks = vos.stream()
                        .collect(Collectors.toMap(SkuStockVo::getSkuId, SkuStockVo::getHasStock));
                vo.setStocks(stocks);
            } else {
                vo.setStocks(Collections.emptyMap());
            }
        });
        CompletableFuture<Void> integrationFuture = CompletableFuture.runAsync(() -> {
            // 获得用户积分
            LinkedHashMap<String, Object> user = LoginUserInterceptor.loginUser.get();
            if (user != null && !user.isEmpty()) {
                Integer integration = (Integer) user.get("integration");
                if (integration != null) {
                    vo.setIntegration(integration);
                } else {
                    vo.setIntegration(0);
                }
            } else {
                vo.setIntegration(0);
            }
        }, threadPoolExecutor);
        try {
            CompletableFuture.allOf(addressFuture, cartItemsFuture, integrationFuture).get();
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch blocklocalO
            e.printStackTrace();
        }
        // 防重复订单令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue()
                .set(OrderConstant.USER_ORDER_TOKEN_PREFIX + LoginUserInterceptor.loginUser.get().get("id"), token, 30,
                        TimeUnit.MINUTES);
        vo.setOrderToken(token);

        return vo;
    }

    /*
     * 验证令牌
     * 通过-> 删除令牌,创建订单号->保存redis->准备分布式闭锁信息
     * 不通过->该订单是否存在->
     * 存在->展示支付选择页,等待闭锁完成
     * 不存在->错误提示页,非法请求
     */
    @Override
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo vo) {
        SubmitOrderResponseVo submitOrderResponseVo = new SubmitOrderResponseVo();
        orderConfirmVoLocal.set(vo);
        submitOrderResponseVo.setCode(0);
        String key = OrderConstant.USER_ORDER_TOKEN_PREFIX + LoginUserInterceptor.loginUser.get().get("id");
        // 1、验证令牌是否合法【令牌的对比和删除必须保证原子性】
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        String orderToken = vo.getOrderToken();
        Long result = redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList(key),
                orderToken);
        ;
        if (result == 0L) {
            // 验证令牌不通过
            submitOrderResponseVo.setCode(1);
        } else {
            OrderCreateTo order = createOrder();
            // 如果两者之间差距小于0.01,则认为对比成功
            if (Math.abs(order.getOrder().getPayAmount().subtract(vo.getPayPrice()).doubleValue()) < 0.01) {
                // 保存订单
                saveOrder(order);
                // 库存锁定,有异常回滚订单
                WareSkuLockVo wareSkuLockVo = new WareSkuLockVo();
                wareSkuLockVo.setOrderSn(order.getOrder().getOrderSn());
                List<OrderItemVo> orderItemVos = order.getOrderItems().stream().map(item -> {
                    OrderItemVo orderItemVo = new OrderItemVo();
                    orderItemVo.setSkuId(item.getSkuId());
                    orderItemVo.setCount(item.getSkuQuantity());
                    orderItemVo.setTitle(item.getSkuName());
                    return orderItemVo;
                }).toList();
                wareSkuLockVo.setLocks(orderItemVos);
                R r = wareFeignService.orderLockStock(wareSkuLockVo);
                if (r.getCode() == 0) {
                    // 锁定成功
                    submitOrderResponseVo.setOrder(order.getOrder());
                } else {
                    // 锁定失败
                    String message = (String) r.get("msg");
                    throw new NoStockException(message);
                }
            } else {
                submitOrderResponseVo.setCode(2);
            }
        }
        return submitOrderResponseVo;
    }

    private void saveOrder(OrderCreateTo order) {
        // TODO Auto-generated method stub
        OrderEntity orderEntity = order.getOrder();
        orderEntity.setModifyTime(new Date());
        this.save(orderEntity);
        List<OrderItemEntity> orderItems = order.getOrderItems();
        orderItemService.saveBatch(orderItems);
    }

    private OrderCreateTo createOrder() {
        OrderCreateTo orderCreateTo = new OrderCreateTo();
        // 生成订单号
        String timeId = IdWorker.getTimeId();
        OrderEntity orderEntity = buildOrder(timeId);

        List<OrderItemEntity> orderItemEntities = buildOrderItems(timeId);
        // 验证价格
        computePrice(orderEntity, orderItemEntities);
        // 保存到实体类
        orderCreateTo.setOrder(orderEntity);
        orderCreateTo.setOrderItems(orderItemEntities);

        return orderCreateTo;
    }

    private void computePrice(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities) {

        // 总价
        BigDecimal total = new BigDecimal("0.0");
        // 优惠价
        BigDecimal coupon = new BigDecimal("0.0");
        BigDecimal intergration = new BigDecimal("0.0");
        BigDecimal promotion = new BigDecimal("0.0");

        // 积分、成长值
        Integer integrationTotal = 0;
        Integer growthTotal = 0;

        // 订单总额，叠加每一个订单项的总额信息
        for (OrderItemEntity orderItem : orderItemEntities) {
            // 优惠价格信息
            coupon = coupon.add(orderItem.getCouponAmount());
            promotion = promotion.add(orderItem.getPromotionAmount());
            intergration = intergration.add(orderItem.getIntegrationAmount());

            // 总价
            total = total.add(orderItem.getRealAmount());

            // 积分信息和成长值信息
            integrationTotal += orderItem.getGiftIntegration();
            growthTotal += orderItem.getGiftGrowth();

        }
        // 1、订单价格相关的
        orderEntity.setTotalAmount(total);
        // 设置应付总额(总额+运费)
        orderEntity.setPayAmount(total.add(orderEntity.getFreightAmount()));
        orderEntity.setCouponAmount(coupon);
        orderEntity.setPromotionAmount(promotion);
        orderEntity.setIntegrationAmount(intergration);

        // 设置积分成长值信息
        orderEntity.setIntegration(integrationTotal);
        orderEntity.setGrowth(growthTotal);

        // 设置删除状态(0-未删除，1-已删除)
        orderEntity.setDeleteStatus(0);
    }

    private List<OrderItemEntity> buildOrderItems(String orderSn) {
        List<OrderItemVo> currentCartItems = cartFeignService.getCurrentCartItems();
        if (currentCartItems != null && currentCartItems.size() > 0) {
            List<OrderItemEntity> collect = currentCartItems.stream().map(item -> {
                OrderItemEntity entity = buildOrderItem(item);
                entity.setOrderSn(orderSn);
                return entity;
            }).collect(Collectors.toList());
            return collect;
        }
        return null;
    }

    private OrderEntity buildOrder(String orderSn) {
        OrderEntity orderEntity = new OrderEntity();

        orderEntity.setOrderSn(orderSn);
        LinkedHashMap<String, Object> linkedHashMap = LoginUserInterceptor.loginUser.get();
        if (linkedHashMap != null && !linkedHashMap.isEmpty()) {
            orderEntity.setMemberId(Long.valueOf((Integer) linkedHashMap.get("id")));

        }

        OrderSubmitVo orderSubmitVo = orderConfirmVoLocal.get();
        // 2) 获取邮费和收件人信息并设置
        R r = wareFeignService.getFare(orderSubmitVo.getAddrId());
        FareVo fareVo = (FareVo) r.getData(new TypeReference<FareVo>() {
        });
        if (fareVo != null) {
            BigDecimal fare = fareVo.getFare();
            orderEntity.setFreightAmount(fare);
            MemberAddressVo address = fareVo.getAddress();
            orderEntity.setReceiverName(address.getName());
            orderEntity.setReceiverPhone(address.getPhone());
            orderEntity.setReceiverPostCode(address.getPostCode());
            orderEntity.setReceiverProvince(address.getProvince());
            orderEntity.setReceiverCity(address.getCity());
            orderEntity.setReceiverRegion(address.getRegion());
            orderEntity.setReceiverDetailAddress(address.getDetailAddress());

            // 3) 设置订单相关的状态信息
            orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
            orderEntity.setConfirmStatus(0);
            orderEntity.setAutoConfirmDay(7);

            return orderEntity;
        } else {
            return null;
        }
    }

    private OrderItemEntity buildOrderItem(OrderItemVo item) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        Long skuId = item.getSkuId();
        // 1) 设置sku相关属性
        orderItemEntity.setSkuId(skuId);
        orderItemEntity.setSkuName(item.getTitle());
        orderItemEntity.setSkuAttrsVals(StringUtils.collectionToDelimitedString(item.getSkuAttrValues(), ";"));
        orderItemEntity.setSkuPic(item.getImage());
        orderItemEntity.setSkuPrice(item.getPrice());
        orderItemEntity.setSkuQuantity(item.getCount());
        // 2) 通过skuId查询spu相关属性并设置
        R r = productFeignService.getSpuInfoBySkuId(skuId);
        if (r.getCode() == 0) {
            SpuInfoVo spuInfo = (SpuInfoVo) r.getData(new TypeReference<SpuInfoVo>() {
            });
            orderItemEntity.setSpuId(spuInfo.getId());
            orderItemEntity.setSpuName(spuInfo.getSpuName());
            orderItemEntity.setSpuBrand(spuInfo.getBrandName());
            orderItemEntity.setCategoryId(spuInfo.getCatalogId());
        }
        // 3) 商品的优惠信息(不做)

        // 4) 商品的积分成长，为价格x数量
        orderItemEntity.setGiftGrowth(item.getPrice().multiply(new BigDecimal(item.getCount())).intValue());
        orderItemEntity.setGiftIntegration(item.getPrice().multiply(new BigDecimal(item.getCount())).intValue());

        // 5) 订单项订单价格信息
        orderItemEntity.setPromotionAmount(BigDecimal.ZERO);
        orderItemEntity.setCouponAmount(BigDecimal.ZERO);
        orderItemEntity.setIntegrationAmount(BigDecimal.ZERO);

        // 6) 实际价格
        BigDecimal origin = orderItemEntity.getSkuPrice().multiply(new BigDecimal(orderItemEntity.getSkuQuantity()));
        BigDecimal realPrice = origin.subtract(orderItemEntity.getPromotionAmount())
                .subtract(orderItemEntity.getCouponAmount())
                .subtract(orderItemEntity.getIntegrationAmount());
        orderItemEntity.setRealAmount(realPrice);

        return orderItemEntity;
    }

    @Override
    public OrderEntity getOrderByOrderSn(String orderSn) {
        // TODO Auto-generated method stub
        OrderEntity order_sn = this.getOne(new QueryWrapper<OrderEntity>().eq("order_sn", orderSn));
        return order_sn;
    }
}