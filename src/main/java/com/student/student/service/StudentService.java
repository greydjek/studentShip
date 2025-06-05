package com.student.student.service;

import com.student.student.data.Student;
import com.student.student.exeption.ErrorMessage;
import com.student.student.exeption.ExceptionData;
import com.student.student.repository.StudentRepository;
import com.student.student.responce.StudentFullProjection;
import com.student.student.responce.student.StudentProjection;
import com.student.student.responce.student.StudentProjectionAndId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public ResponseEntity<Student> findById(UUID uuid) {
        Student student = studentRepository.findById(uuid).orElseThrow(
                () -> new ExceptionData(ErrorMessage.STUDENT_NOT_FOUND));
        return ResponseEntity.ok(student);
    }

    public List<StudentProjectionAndId> findAllProjectionForFront() {
        return studentRepository.findAllProjectionForFront();
    }

    @Transactional(readOnly = true)
    public Page<StudentProjectionAndId> findAllStudentPage(Pageable pageable) {
        try {
            log.info(studentRepository.findAllForProjection(pageable).toString());
            return studentRepository.findAllForProjection(pageable);
        } catch (ExceptionData e) {
            throw new ExceptionData(ErrorMessage.STUDENT_NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    public Page<StudentFullProjection> findAllStudentForMainPage(Pageable pageable) {
        try {
            log.info(studentRepository.findAllForMainPage(pageable).toString());
            return studentRepository.findAllForMainPage(pageable);
        } catch (ExceptionData e) {
            throw new ExceptionData(ErrorMessage.STUDENT_NOT_FOUND);
        }
    }

    public ResponseEntity saveNewStudent(Student student) {
        if (!student.getFirstName().isBlank() ||
            !student.getLastName().isBlank() ||
            !student.getSpecialization().isBlank() ||
            student.getCourse() <= 0) {
            studentRepository.save(student);
        } else {
            throw new ExceptionData(ErrorMessage.DATA_STUDENT_NOT_NULL);
        }
        return ResponseEntity.ok().build();
    }

    //todo проверить приходящую строку id
    public ResponseEntity<Student> deleteStudentById(UUID uuid) {
        Optional<Student> student = studentRepository.findById(uuid);
        if (student.isPresent()) {
            studentRepository.deleteById(uuid);
            return ResponseEntity.ok(student.get());
        } else {
            throw new ExceptionData(ErrorMessage.STUDENT_NOT_FOUND);
        }
    }

    @Transactional
    public ResponseEntity<StudentFullProjection> refreshDataStudent(Student studentRef) {
        Student student = studentRepository.findById(studentRef.getId()).orElseThrow(
                () -> new ExceptionData(ErrorMessage.STUDENT_NOT_FOUND));
if (studentRef.getFirstName().isBlank()||
    studentRef.getLastName().isBlank()||
    studentRef.getCourse() <= 0||
    studentRef.getCourse() > 4) {
    throw new ExceptionData(ErrorMessage.DATA_STUDENT_NOT_NULL);
}
        student.setFirstName(studentRef.getFirstName());
        student.setLastName(studentRef.getLastName());
        student.setMiddleName(studentRef.getMiddleName());
        student.setCourse(studentRef.getCourse());
        student.setSpecialization(studentRef.getSpecialization());
        student.setMobilePhone(studentRef.getMobilePhone());
        student.setWorker(studentRef.getWorker());
        return ResponseEntity.ok(studentRepository.findByIdFullProjection(student.getId()).orElseThrow(() -> new ExceptionData(ErrorMessage.STUDENT_NOT_FOUND)));
    }

    public ResponseEntity<List<StudentProjectionAndId>> findByLikeNameStudent(String likeName) {
        List<StudentProjectionAndId> studentProjectionAndId = studentRepository.findByLikeNameStudent(likeName);
        if (studentProjectionAndId.isEmpty()) {
            throw new ExceptionData(ErrorMessage.NOT_FOUND_LIKE_NAME);
        }
        return ResponseEntity.ok(studentProjectionAndId.stream().toList());
    }

    public ResponseEntity<List<StudentFullProjection>> findByLikeNameFullProjectionStudent(String likeName) {
        List<StudentFullProjection> studentFullProjection = studentRepository.findByLikeNameFullProjectionStudent(likeName);
        if (studentFullProjection.isEmpty()) {
            throw new ExceptionData(ErrorMessage.NOT_FOUND_LIKE_NAME);
        }
        return ResponseEntity.ok(studentFullProjection.stream().toList());
    }
}
