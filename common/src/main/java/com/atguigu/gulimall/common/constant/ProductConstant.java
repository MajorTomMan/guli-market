/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-09-07 17:20:02
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-02-12 14:28:22
 * @FilePath: /GuliMall/common/src/main/java/com/atguigu/gulimall/common/constant/ProductConstant.java
 * @Description: 
 * 
 * Copyright (c) 2022 by flashnames 765719516@qq.com, All Rights Reserved. 
 */
package com.atguigu.gulimall.common.constant;

import lombok.Data;

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
    }
    public enum StatusEnum{
        NEW_SPU(0,"新建"),
        SPU_UP(1,"商品上架"),
        SPU_DOWN(2,"商品下架");
        private int code;
        private String msg;
                /**
         * @param code
         * @param msg
         */
        private StatusEnum(int code, String msg) {
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
    }
}
