/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.app.controller;

import io.renren.common.utils.R;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.annotation.LoginUser;
import io.renren.modules.app.entity.UserEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * APP测试接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/app")
@Tag(name = "APP测试接口")
public class AppTestController {

    @Login
    @GetMapping("userInfo")
    @Operation(summary = "获取用户信息")
    public R userInfo(@LoginUser UserEntity user) {
        return R.ok().put("user", user);
    }

    @Login
    @GetMapping("userId")
    @Operation(summary = "获取用户ID")
    public R userInfo(@RequestAttribute("userId") Integer userId) {
        return R.ok().put("userId", userId);
    }

    @GetMapping("notToken")
    @Operation(summary = "忽略Token验证测试")
    public R notToken() {
        return R.ok().put("msg", "无需token也能访问。。。");
    }

}
