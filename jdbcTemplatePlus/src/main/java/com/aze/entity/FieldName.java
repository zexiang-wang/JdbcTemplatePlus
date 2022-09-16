package com.aze.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)//标记注解的使用范围
@Retention(RetentionPolicy.RUNTIME)//标记主键的有效期
public @interface FieldName {
    String value();//记录数据库表重的字段名称
}