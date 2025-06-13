package com.student.student.controller;

import com.student.student.data.Student;
import com.student.student.responce.StudentFullProjection;
import com.student.student.responce.student.StudentProjection;
import com.student.student.responce.student.StudentProjectionAndId;
import com.student.student.service.StudentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.atmosphere.config.service.Put;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/")
    public ResponseEntity<?> getAllStudent(@PageableDefault(size = 11, sort = "firstName")Pageable pageable) {
        Page<StudentProjectionAndId> page = studentService.findAllStudentPage(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/find_like")
    public ResponseEntity<List<StudentProjectionAndId>> findByLikeNameStudentProjection(@RequestParam String likeName) {
        return studentService.findByLikeNameStudent(likeName);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> findStudentById(@PathVariable("id") UUID uuid) {
        return studentService.findById(uuid);
    }

    @PostMapping("/")
    public ResponseEntity<StudentProjection> createNewStudent(@RequestBody Student student) {
        return studentService.saveNewStudent(student);
    }

    @PutMapping("/")
    public ResponseEntity<StudentFullProjection> refreshDataStudent(@RequestBody Student student) {
        return studentService.refreshDataStudent(student);
    }

    @DeleteMapping("/")
    public ResponseEntity<Student> deleteStudentById(@RequestParam UUID uuid) {
        return studentService.deleteStudentById(uuid);
    }
}
