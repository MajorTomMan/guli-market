/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-10-31 22:44:55
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-10-31 22:51:04
 * @FilePath: \Guli\auth\src\main\java\com\atguigu\gulimall\auth\vo\UserRegisterVo.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.auth.vo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class UserRegisterVo {
    @NotEmpty(message = "用户名必须提交")
    @Length(min = 6, max = 18, message = "用户名必须是6-18位字符")
    private String userName;
    @NotEmpty(message = "密码必须提交")
    @Length(min = 6, max = 18, message = "密码必须是6-18位字符")
    private String password;
    @Pattern(regexp = "^[1]([3-9])[0-9]{9}$", message = "手机号格式不正确")
    private String phone;
    @NotEmpty(message = "验证码必须填写")
    private String code;
}
