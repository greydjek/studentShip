package com.student.student.service.UtilsSevices;

import com.student.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentCourseService {

    private final StudentRepository studentRepository;

    @Scheduled(cron = "${student.schedule.annual-report.cron}")
    @Transactional
    public void incrementStudentCourses() {
        studentRepository.incrementCourses();
    }
}
