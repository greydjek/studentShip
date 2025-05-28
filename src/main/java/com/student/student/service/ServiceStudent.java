package com.student.student.service;

import com.student.student.data.Student;
import com.student.student.exeption.ErrorMessage;
import com.student.student.exeption.MyExceptionHandler;
import com.student.student.repository.StudentRepository;
import com.student.student.repository.responce.StudentProjection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceStudent {

    private final StudentRepository studentRepository;

    //todo дописать Исключения
    public Student findById(UUID uuid) {
        return studentRepository.findById(uuid).orElseThrow(RuntimeException::new);
    }

    @Transactional(readOnly = true)
    public List<String> findAllStudentNameLastName() {
        List<String> list;
        try {
            list = studentRepository.findAllProjectedBy().map(StudentProjection::getFullName).toList();
            log.info(list.toString());
            return list;
        } catch (Exception e) {
            throw new MyExceptionHandler(ErrorMessage.USER_NOT_FOUND);
        }
    }
}
