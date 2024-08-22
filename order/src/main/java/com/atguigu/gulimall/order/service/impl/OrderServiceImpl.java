package com.atguigu.gulimall.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.order.dao.OrderDao;
import com.atguigu.gulimall.order.entity.OrderEntity;
import com.atguigu.gulimall.order.feign.CartFeignService;
import com.atguigu.gulimall.order.feign.MemberFeignService;
import com.atguigu.gulimall.order.interceptor.LoginUserInterceptor;
import com.atguigu.gulimall.order.service.OrderService;
import com.atguigu.gulimall.order.vo.MemberAddressVo;
import com.atguigu.gulimall.order.vo.OrderConfirmVo;
import com.atguigu.gulimall.order.vo.OrderItemVo;

@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
    @Autowired
    private MemberFeignService memberFeignService;
    @Autowired
    private CartFeignService cartFeignService;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

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
        // 解决异步任务丢失线程请求头的问题,将request属性共享给子线程
        RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(), true);
        // 进行异步改造
        CompletableFuture<Void> addressFuture = CompletableFuture.runAsync(() -> {
            // 获得地址页
            LinkedHashMap<String, Object> user = (LinkedHashMap<String, Object>) LoginUserInterceptor.loginUser.get();
            if (user != null && !user.isEmpty()) {
                Long memberId = (Long) user.get("memberId");
                if (memberId != null) {
                    List<MemberAddressVo> address = memberFeignService.getAddress(memberId);
                    vo.setMemberAddressVos(address);
                }
            }

        }, threadPoolExecutor);
        CompletableFuture<Void> cartItemsFuture = CompletableFuture.runAsync(() -> {
            // 获得购物车
            List<OrderItemVo> currentCartItems = cartFeignService.getCurrentCartItems();
            if (currentCartItems != null && !currentCartItems.isEmpty()) {
                vo.setItems(currentCartItems);
            }

        }, threadPoolExecutor);
        CompletableFuture<Void> integrationFuture = CompletableFuture.runAsync(() -> {
            // 获得用户积分
            LinkedHashMap<String, Object> user = LoginUserInterceptor.loginUser.get();
            if (user != null && !user.isEmpty()) {
                Integer integration = (Integer) user.get("integration");
                vo.setIntegration(integration);
            }
        }, threadPoolExecutor);
        try {
            CompletableFuture.allOf(addressFuture, cartItemsFuture, integrationFuture).get();
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return vo;
    }

}