package com.aze.service.impl;

import com.aze.MyJdbcTemplate;
import com.aze.entity.DataFilter;
import com.aze.entity.Student;
import com.aze.service.StudentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private MyJdbcTemplate myJdbcTemplate;

    @Override
    public List<Student> queryAllStudent() {
        DataFilter dataFilter = new DataFilter<>(Student.class);
        return myJdbcTemplate.list(dataFilter);
    }

    @Override
    public List<Student> queryStudentByAge(Integer age) {
        DataFilter dataFilter = new DataFilter<>(Student.class);
        dataFilter.eq("studentDept",age);
        return myJdbcTemplate.list(dataFilter);
    }

    @Override
    public int insertStudent(Student student) {
        return myJdbcTemplate.insert(student);
    }

    @Override
    public int deleteStudentByNo(Long studentNo) {
        DataFilter dataFilter = new DataFilter(Student.class);
        dataFilter.eq("studentNo",studentNo);
        return myJdbcTemplate.delete(dataFilter);
    }

    @Override
    public int updateStudent(Student student) {
        Student studentToUpdate = new Student();
        BeanUtils.copyProperties(student,studentToUpdate);
        studentToUpdate.setStudentNo(null);
        DataFilter dataFilter = new DataFilter(Student.class);
        dataFilter.eq("studentNo",student.getStudentNo());
        return myJdbcTemplate.update(studentToUpdate,dataFilter);
    }
}
