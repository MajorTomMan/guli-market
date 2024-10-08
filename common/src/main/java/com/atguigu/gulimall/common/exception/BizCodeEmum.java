package com.atguigu.gulimall.common.exception;

public enum BizCodeEmum {
    UNKNOW_EXCEPTION(10000,"系统未知异常"),
    VAILD_EXCEPTION(10001,"参数格式校验失败"),
    TO_MANY_REQUEST(10002,"请求流量过大，请稍后再试"),
    SMS_CODE_EXCEPTION(10002,"验证码获取频率太高，请稍后再试"),
    PRODUCT_UP_EXCEPTION(11000,"商品上架异常"),
    USER_EXIST_EXCEPTION(15001,"存在相同的用户"),
    PHONE_EXIST_EXCEPTION(15002,"存在相同的手机号"),
    NO_STOCK_EXCEPTION(21000,"商品库存不足"),
    LOGINACCT_PASSWORD_EXCEPTION(15003,"账号或密码错误");

    private int code;
    private String msg;

    /**
     * @param code 错误码
     * @param msg  错误消息
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
