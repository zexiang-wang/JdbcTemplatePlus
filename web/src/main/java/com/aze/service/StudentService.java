package com.aze.service;

import com.aze.entity.Student;

import java.util.List;

public interface StudentService {

    List<Student> queryAllStudent();
    List<Student> queryStudentByAge(Integer age);

    int insertStudent(Student student);
    int deleteStudentByNo(Long studentNo);
    int updateStudent(Student student);

}
