package com.aze.controller;

import com.aze.entity.Student;
import com.aze.service.StudentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private StudentService studentService;

    @PostMapping
    @RequestMapping("/queryAllStudent")
    public List<Student> queryAllStudent(){
        return studentService.queryAllStudent();
    }

    @PostMapping
    @RequestMapping("/queryStudentByAge")
    public List<Student> queryStudentByAge(Integer age){
        return studentService.queryStudentByAge(age);
    }
    @PostMapping
    @RequestMapping("/insertStudent")
    public Integer insertStudent(Student student){
        return studentService.insertStudent(student);
    }
    @PostMapping
    @RequestMapping("/deleteStudentByNo")
    public Integer deleteStudentByNo(Long studentNo){
        return studentService.deleteStudentByNo(studentNo);
    }
    @PostMapping
    @RequestMapping("/updateStudent")
    public Integer updateStudent(Student student){
        return studentService.updateStudent(student);
    }
}
