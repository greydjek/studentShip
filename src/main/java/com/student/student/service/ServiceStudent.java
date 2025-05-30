package com.student.student.service;

import com.student.student.data.Student;
import com.student.student.exeption.ErrorMessage;
import com.student.student.exeption.ExceptionData;
import com.student.student.repository.StudentRepository;
import com.student.student.repository.responce.StudentProjection;
import com.student.student.repository.responce.StudentProjectionAndId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceStudent {

    private final StudentRepository studentRepository;

    public ResponseEntity<Student> findById(UUID uuid) {
        Student student = studentRepository.findById(uuid).orElseThrow(
                () -> new ExceptionData(ErrorMessage.STUDENT_NOT_FOUND));
        return ResponseEntity.ok(student);
    }

    @Transactional(readOnly = true)
    public Page<StudentProjectionAndId> findAllStudentPage(int number, int size) {
        try {
            Pageable page = PageRequest.of(number, size);
            log.info(studentRepository.findAllForProjection(page).toString());
            return studentRepository.findAllForProjection(page);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionData(ErrorMessage.STUDENT_NOT_FOUND);
        }
    }

    public ResponseEntity<StudentProjection> saveNewStudent(Student student) {
        if (!student.getFirstName().isBlank() ||
            !student.getLastName().isBlank() ||
            !student.getSpecialization().isBlank() ||
            student.getCourse() <= 0) {
            studentRepository.save(student);
        } else {
            throw new ExceptionData(ErrorMessage.DATA_STUDENT_NOT_NULL);
        }
        return studentRepository.findByIdProjection(student.getId());
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
    public ResponseEntity<StudentProjection> refreshDataStudent(Student studentRef) {
        Student student = studentRepository.findById(studentRef.getId()).orElseThrow(
                () -> new ExceptionData(ErrorMessage.STUDENT_NOT_FOUND));

        student.setFirstName(studentRef.getFirstName());
        student.setLastName(studentRef.getLastName());
        student.setMiddleName(studentRef.getMiddleName());
        student.setCourse(studentRef.getCourse());
        student.setSpecialization(studentRef.getSpecialization());
        student.setMobilePhone(studentRef.getMobilePhone());
        return ResponseEntity.ok(studentRepository.findByIdProjection(student.getId()).getBody());
    }

    public ResponseEntity<List<StudentProjectionAndId>> findByLikeNameStudent(String likeName) {
        List<StudentProjectionAndId> studentProjectionAndId = studentRepository.findByLikeNameStudent(likeName);
        if (studentProjectionAndId.isEmpty()) {
            throw new ExceptionData(ErrorMessage.NOT_FOUND_LIKE_NAME);
        }
        return ResponseEntity.ok(studentProjectionAndId.stream().toList());
    }
}
