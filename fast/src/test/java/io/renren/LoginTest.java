package io.renren;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysUserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginTest {
    @Autowired
    SysUserService service;
    @Test
    public void testPassword(){
        SysUserEntity user = service.queryByUserName("root");
        user.setUsername("root");
        user.setPassword("root");
        service.update(user);
    }
}
