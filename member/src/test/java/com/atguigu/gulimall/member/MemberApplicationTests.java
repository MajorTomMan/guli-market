package com.atguigu.gulimall.member;

import java.util.List;

import com.atguigu.gulimall.member.entity.MemberEntity;
import com.atguigu.gulimall.member.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberApplicationTests {
    @Autowired
    MemberService service;
    @Test
    void contextLoads() {
        List<MemberEntity> list=service.list(new QueryWrapper<MemberEntity>());
        list.forEach((item)->{
            System.out.println(item);
        });
    }

}
