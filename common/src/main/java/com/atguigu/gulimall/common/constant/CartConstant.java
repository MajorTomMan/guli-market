package com.atguigu.gulimall.common.constant;

import java.time.Duration;

public class CartConstant {

    public final static String TEMP_USER_COOKIE_NAME = "user-key";

    public final static Integer TEMP_USER_COOKIE_TIMEOUT = (int) Duration.ofHours(1).getSeconds();

    public final static String CART_PREFIX = "gulimall:cart:";
}
