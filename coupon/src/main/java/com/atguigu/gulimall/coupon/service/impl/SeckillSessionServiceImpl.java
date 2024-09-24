package com.atguigu.gulimall.coupon.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.coupon.dao.SeckillSessionDao;
import com.atguigu.gulimall.coupon.entity.SeckillSessionEntity;
import com.atguigu.gulimall.coupon.entity.SeckillSkuRelationEntity;
import com.atguigu.gulimall.coupon.service.SeckillSessionService;
import com.atguigu.gulimall.coupon.service.SeckillSkuRelationService;

@Service("seckillSessionService")
public class SeckillSessionServiceImpl extends ServiceImpl<SeckillSessionDao, SeckillSessionEntity>
        implements SeckillSessionService {
    @Autowired
    private SeckillSkuRelationService seckillSkuRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SeckillSessionEntity> page = this.page(
                new Query<SeckillSessionEntity>().getPage(params),
                new QueryWrapper<SeckillSessionEntity>());

        return new PageUtils(page);
    }

    /*
     * 查找在开始至结束时间段内开始的活动
     */
    @Override
    public List<SeckillSessionEntity> getLatest3DaySession() {
        // TODO Auto-generated method stub
        List<SeckillSessionEntity> list = this
                .list(new QueryWrapper<SeckillSessionEntity>().between("start_time", startTime(), endTime()));
        if (list != null && !list.isEmpty()) {
            List<SeckillSessionEntity> entities = list.stream().map((entity -> {
                Long id = entity.getId();
                List<SeckillSkuRelationEntity> relations = seckillSkuRelationService
                        .list(new QueryWrapper<SeckillSkuRelationEntity>().eq("promotion_session_id", id));
                entity.setRelationEntities(relations);
                return entity;
            })).toList();
            return entities;
        }
        return null;
    }

    private String endTime() {
        LocalDateTime now = LocalDateTime.now().plusDays(2);
        LocalDateTime max = now.with(LocalTime.MAX);
        // 直接使用 ISO_LOCAL_DATE_TIME 格式化
        return max.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private String startTime() {
        LocalDateTime now = LocalDateTime.now().minusDays(100);
        LocalDateTime min = now.with(LocalTime.MIN);
        // 直接使用 ISO_LOCAL_DATE_TIME 格式化
        return min.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}