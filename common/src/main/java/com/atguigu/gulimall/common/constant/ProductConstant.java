/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-09-07 17:20:02
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2022-12-27 17:59:10
 * @FilePath: /common/src/main/java/com/atguigu/gulimall/common/constant/ProductConstant.java
 * @Description: 
 * 
 * Copyright (c) 2022 by flashnames 765719516@qq.com, All Rights Reserved. 
 */
package com.atguigu.gulimall.common.constant;

public class ProductConstant {
    public enum AttrEnum{
        ATTR_TYPE_BASE(1,"基本属性"),
        ATTR_TYPE_SALE(0,"销售属性");
        private int code;
        private String msg;
        /**
         * @param code
         * @param msg
         */
        private AttrEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
                /**
         * @return the code
         */
        public int getCode() {
            return code;
        }
        /**
         * @param code the code to set
         */
        public void setCode(int code) {
            this.code = code;
        }
                /**
         * @return the msg
         */
        public String getMsg() {
            return msg;
        }
        /**
         * @param msg the msg to set
         */
        public void setMsg(String msg) {
            this.msg = msg;
        }
    };
}
