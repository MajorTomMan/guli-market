package com.atguigu.gulimall.seckill;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.atguigu.gulimall.seckill.schedule.SecKillScheduled;
@SpringBootTest
class SeckillApplicationTests {
	@Autowired
	SecKillScheduled schedule;
	@Test
	public void testUpload(){
		schedule.uploadSecKillSkuLatest3Days();
	}
	public void testUpdate(){
		
	}
}
