package com.atguigu.gulimall.common.valid;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {
    private Set<Integer> set=new HashSet<>();
    @Override
    public void initialize(ListValue constraintAnnotation) {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        return set.contains(value);
    }
    
}
