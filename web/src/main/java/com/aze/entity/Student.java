package com.aze.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@TableName("student")
public class Student {
    private Long studentNo;
    private String studentName;
    private int studentDept;
    private int studentClass;
    private int age;
}
