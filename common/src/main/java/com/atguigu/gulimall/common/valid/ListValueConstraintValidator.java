/*
 * @Date: 2023-06-23 17:37:37
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2024-07-29 00:01:11
 * @FilePath: \Guli\common\src\main\java\com\atguigu\gulimall\common\valid\ListValueConstraintValidator.java
 * @Description: MajorTomMan @版权声明 保留文件所有权利
 */
package com.atguigu.gulimall.common.valid;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {
    private Set<Integer> set=new HashSet<>();
    @Override
    public void initialize(ListValue constraintAnnotation) {

        int[] values = constraintAnnotation.values();
        for (int value : values) {
            set.add(value);
        }
    }
    /**
     * @param value 需要校验的值
     * @param content
     * @return
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {

        return set.contains(value);
    }
    
}
