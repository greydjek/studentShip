package com.student.student.controller;

import com.student.student.data.Student;
import com.student.student.responce.student.StudentProjection;
import com.student.student.responce.student.StudentProjectionAndId;
import com.student.student.service.ServiceStudent;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/")
    public ResponseEntity<?> getAllStudent(@PageableDefault(size = 11, sort = "firstName")Pageable pageable) {
        Page<StudentProjectionAndId> page = serviceStudent.findAllStudentPage(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/find_like")
    public ResponseEntity<List<StudentProjectionAndId>> findByLikeNameStudentProjection(@RequestParam String likeName) {
        return serviceStudent.findByLikeNameStudent(likeName);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> findStudentById(@PathVariable("id") UUID uuid) {
        return serviceStudent.findById(uuid);
    }

    @PostMapping("/")
    public ResponseEntity<StudentProjection> createNewStudent(@RequestBody Student student) {
        return serviceStudent.saveNewStudent(student);
    }

    @GetMapping("/")
    public ResponseEntity<StudentProjection> refreshDataStudent(@RequestParam Student student) {
        return serviceStudent.refreshDataStudent(student);
    }

    @DeleteMapping("/")
    public ResponseEntity<Student> deleteStudentById(@RequestParam UUID uuid) {
        return serviceStudent.deleteStudentById(uuid);
    }
}
