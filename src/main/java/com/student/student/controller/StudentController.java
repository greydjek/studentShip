package com.student.student.controller;

import com.student.student.data.Student;
import com.student.student.repository.responce.StudentProjection;
import com.student.student.service.ServiceStudent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final ServiceStudent serviceStudent;

    @PostMapping("/new_st")
    public ResponseEntity<StudentProjection> createNewStudent(@RequestBody Student student) {
        return serviceStudent.saveNewStudent(student);
    }

    @GetMapping("/all")
    public List<String> getAllStudent() {
        return serviceStudent.findAllStudentNameLastName();
    }

    @DeleteMapping("/delete_st")
    public HttpStatus deleteStudentById(@RequestParam String id) {
        return serviceStudent.deleteStudentById(id);
    }
}
