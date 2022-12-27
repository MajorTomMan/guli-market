/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-12-27 17:51:05
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2022-12-27 18:05:51
 * @FilePath: /common/src/main/java/com/atguigu/gulimall/common/constant/WareConstant.java
 * @Description: 
 * 
 * Copyright (c) 2022 by flashnames 765719516@qq.com, All Rights Reserved. 
 */
package com.atguigu.gulimall.common.constant;

public class WareConstant {
    public enum PurchaseStatusEnum {
        CREATED(0, "新建"), ASSIGNED(1, "已分配"),
        RECEIVE(2, "已领取"), FINISH(3, "已完成"),
        HASEERROR(4, "有异常");

        private int code;
        private String msg;

        /**
         * @param code
         * @param msg
         */
        private PurchaseStatusEnum(int code, String msg) {
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
    public enum PurchaseDetailStatusEnum {
        CREATED(0, "新建"), ASSIGNED(1, "已分配"),
        BUYING(2, "正在采购"), FINISH(3, "已完成"),
        HASEERROR(4, "采购失败");

        private int code;
        private String msg;

        /**
         * @param code
         * @param msg
         */
        private PurchaseDetailStatusEnum(int code, String msg) {
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
