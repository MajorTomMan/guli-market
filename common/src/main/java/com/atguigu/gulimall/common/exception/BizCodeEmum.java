package com.atguigu.gulimall.common.exception;

public enum BizCodeEmum {
    UNKNOWN_EXCEPTION(10000,"系统未知异常"),
    VALID_EXCEPTION(10001,"参数校验失败"),
    SMS_CODE_EXCEPTION(10002,"验证码获取频率太高,请稍后再试"),
    PRODUCT_UP_EXCEPTION(11000,"商品上架异常");
    private int code;
    private String msg;
        /**
     * @param code 错误码
     * @param msg 错误消息
     */
    private BizCodeEmum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    /**
     * @return 错误码
     */
    public int getCode() {
        return code;
    }
    /**
     * @return 错误信息
     */
    public String getMsg() {
        return msg;
    }
}
