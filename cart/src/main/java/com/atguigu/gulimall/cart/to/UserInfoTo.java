package com.atguigu.gulimall.cart.to;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
 public class UserInfoTo {
 
     private Long userId;
 
     private String userKey;
 
     /**
      * 是否临时用户
      */
     private Boolean tempUser = false;
 
 }
