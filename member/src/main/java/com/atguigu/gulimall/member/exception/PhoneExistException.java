/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-11-05 18:33:44
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-11-05 18:33:48
 * @FilePath: \Guli\member\src\main\java\com\atguigu\gulimall\member\exception\PhoneExistException.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.member.exception;

public class PhoneExistException extends RuntimeException{
    public PhoneExistException(){
        super("手机号不存在");
    }
}
