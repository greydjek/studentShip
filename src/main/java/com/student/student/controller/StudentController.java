package com.student.student.controller;

import com.student.student.service.ServiceStudent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final ServiceStudent serviceStudent;

    @GetMapping("/all")
    public List<String> getAllStudent(){
        return serviceStudent.findAllStudentNameLastName();
    }
}
