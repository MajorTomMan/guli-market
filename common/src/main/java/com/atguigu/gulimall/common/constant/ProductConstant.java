package com.atguigu.gulimall.common.constant;

public class ProductConstant {
    public enum AttrEnum{
        ATTR_TYPE_BASE(1,"基本属性"),ATTR_TYPE_SALE(0,"销售属性");
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
