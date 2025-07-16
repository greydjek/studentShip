package com.student.student.service;

import com.student.student.data.Company;
import com.student.student.data.InternShip;
import com.student.student.data.Student;
import com.student.student.exeption.ErrorMessage;
import com.student.student.exeption.ExceptionData;
import com.student.student.repository.InternShipRepository;
import com.student.student.request.InternshipDtoForFront;
import com.student.student.request.RequestDtoInternShip;
import com.student.student.responce.internShip.DtoResponseInternShip;
import com.student.student.responce.internShip.InternShipProjection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class InternshipService {

    private final InternShipRepository internShipRepository;

    private final StudentService studentService;

    private final CompanyService companyService;

    public ResponseEntity<InternShipProjection> getInternShipFromId(UUID id) {
        InternShipProjection internShipProjection =
                internShipRepository.findByInternshipProjection(id)
                        .orElseThrow(() -> new ExceptionData
                                (ErrorMessage.INTERN_SHIP_NOT_FOUND));
        return ResponseEntity.ok(internShipProjection);
    }

    @Transactional
    public ResponseEntity<InternShip> createInternShip(InternshipDtoForFront requestDtoInternShip) {
        Student student = studentService.findById(requestDtoInternShip.studentId()).getBody();
        Company company = companyService.findById(requestDtoInternShip.companyId());

        log.info("студент найден, компания проверена");

        InternShip internShip = InternShip.builder()
                .studentId(student)
                .companyId(company)
                .startData(requestDtoInternShip.startDate())
                .endData(requestDtoInternShip.endDate())
                .position(requestDtoInternShip.position())
                .comments(requestDtoInternShip.comments())
                .build();

        internShipRepository.save(internShip);
        return ResponseEntity.ok(internShip);
    }

    @Transactional
    public ResponseEntity<?> refactorInternShip(RequestDtoInternShip internShipRefactor) {
        InternShip internShip = internShipRepository.findById(internShipRefactor.id()).orElseThrow(
                () -> new ExceptionData
                        (ErrorMessage.INTERN_SHIP_NOT_FOUND));

        Student student = studentService.findById(internShipRefactor.studentId()).getBody();
        Company company = companyService.findById(internShipRefactor.companyId());
        log.info("сервис рефакторинг практики проверил студента и компанию");
        internShip.setCompanyId(company);
        internShip.setStudentId(student);
        internShip.setStartData(internShipRefactor.startDate());
        internShip.setEndData(internShipRefactor.endDate());
        internShip.setPosition(internShipRefactor.position());
        internShip.setComments(internShipRefactor.comments());
        log.info("рефакторинг сохранил изменения в сущность практики");
        return ResponseEntity.ok(internShip);
    }

    @Transactional
    public ResponseEntity<?> deleteById(UUID uuid) {
        InternShip internShip = internShipRepository.findById(uuid).orElseThrow(() -> new ExceptionData
                (ErrorMessage.INTERN_SHIP_NOT_FOUND));
        internShipRepository.deleteById(uuid);
        return ResponseEntity.ok(internShip);
    }

    public Page<DtoResponseInternShip> getAllInternShip(Pageable pageable) {
        return internShipRepository.findAllRequestDto(pageable);
    }

    public Page<DtoResponseInternShip> getLikeStudentName(String name, Pageable pageable) {
        return internShipRepository.findAllRequestDtoForLikeStudentName(name, pageable);
    }

    public DtoResponseInternShip getDtoResponseInternShip(UUID internshipId) {
        log.info("в сервис предано Id компании {}", internshipId);
        return internShipRepository.findByIdDtoResponseInternship(internshipId);
    }

    public String getComments(UUID id) {
        return internShipRepository.getCommentsById(id);
    }
}
