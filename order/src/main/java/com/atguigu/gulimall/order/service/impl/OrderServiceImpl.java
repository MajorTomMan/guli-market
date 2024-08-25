package com.atguigu.gulimall.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.order.constant.OrderConstant;
import com.atguigu.gulimall.order.dao.OrderDao;
import com.atguigu.gulimall.order.entity.OrderEntity;
import com.atguigu.gulimall.order.feign.CartFeignService;
import com.atguigu.gulimall.order.feign.MemberFeignService;
import com.atguigu.gulimall.order.feign.WareFeignService;
import com.atguigu.gulimall.order.interceptor.LoginUserInterceptor;
import com.atguigu.gulimall.order.service.OrderService;
import com.atguigu.gulimall.order.to.OrderCreateTo;
import com.atguigu.gulimall.order.vo.MemberAddressVo;
import com.atguigu.gulimall.order.vo.OrderConfirmVo;
import com.atguigu.gulimall.order.vo.OrderItemVo;
import com.atguigu.gulimall.order.vo.OrderSubmitVo;
import com.atguigu.gulimall.order.vo.SkuStockVo;
import com.atguigu.gulimall.order.vo.SubmitOrderResponseVo;

@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
    @Autowired
    private ThreadLocal<OrderSubmitVo> orderConfirmVoLocal = new ThreadLocal<>();
    @Autowired
    private MemberFeignService memberFeignService;
    @Autowired
    private CartFeignService cartFeignService;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    private WareFeignService wareFeignService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>());

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVo confirmOrder() {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        String key = OrderConstant.USER_ORDER_TOKEN_PREFIX + LoginUserInterceptor.loginUser.get().get("id");
        // 1、验证令牌是否合法【令牌的对比和删除必须保证原子性】
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        String orderToken = vo.getOrderToken();
        Long result = redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList(key), orderToken);;
        if(result==0L){
            // 验证令牌不通过
            submitOrderResponseVo.setCode(1);
        }
        else{
            OrderCreateTo order = createOrder(vo.getAddrId());
        }
        return submitOrderResponseVo;
    }

    private OrderCreateTo createOrder(Long addrId){
        OrderCreateTo createTo=new OrderCreateTo();
        String timeId = IdWorker.getTimeId();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(timeId);
        R r = wareFeignService.getFare(addrId);
        return null;
    }
}