package com.student.student.service;

import com.student.student.data.Company;
import com.student.student.exeption.ErrorMessage;
import com.student.student.exeption.ExceptionData;
import com.student.student.repository.CompanyRepository;
import com.student.student.responce.company.CompanyProjection;
import com.student.student.responce.company.CompanyProjectionAndId;
import com.student.student.responce.company.CompanyResponceRecord;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ServiceCompany {

    private final CompanyRepository companyRepository;

    //todo проверка данных полей
    @Transactional
    public ResponseEntity<Company> saveNewCompany(Company company) {
        if (company.getAddress().isBlank() ||
            company.getName().isBlank()) {
            throw new ExceptionData
                    (ErrorMessage.DATA_COMPANY_NOT_NULL);
        }
        companyRepository.save(company);
        log.info("сохранен новый объект в БД {}", company);
        return ResponseEntity.ok(company);
    }

    public Page<CompanyProjectionAndId> findAll(Pageable pageable) {
        return companyRepository.findAllForProjection(pageable);
    }

    public ResponseEntity<String> findByName(String name) {
        return companyRepository.findByName(name).map(CompanyProjection::getCompanyProjection)
                .map(
                        companyProjection -> {

                            log.info("нашли компанию по имени {}", companyProjection);
                            return ResponseEntity.ok(companyProjection);
                        }).orElseThrow(() ->
                {
                    log.info("не нашли компанию по имени проверьте инф");
                    throw new ExceptionData
                            (HttpStatus.NOT_FOUND, ErrorMessage.COMPANY_NOT_FOUND);
                });
    }

    public ResponseEntity<CompanyResponceRecord> deleteById(UUID uuid) {
        CompanyResponceRecord companyResponceRecord = companyRepository.findByIdProjection(uuid)
                .orElseThrow(() -> new ExceptionData(ErrorMessage.COMPANY_NOT_FOUND));
        companyRepository.deleteById(uuid);
        return ResponseEntity.ok(companyResponceRecord);
    }

    public Company findById(UUID id) {
        return companyRepository.findById(id).orElseThrow(() -> new ExceptionData
                (ErrorMessage.COMPANY_NOT_FOUND));
    }

    @Transactional
    public ResponseEntity<Company> refactor(Company refCompany) {
        Company company = companyRepository.findById(refCompany.getId()).orElseThrow(() -> new ExceptionData
                (ErrorMessage.COMPANY_NOT_FOUND));
        log.info("найден объект компании {}", company.toString());
        company.setName(refCompany.getName());
        company.setAddress(refCompany.getAddress());
        log.info("обновленный объект компании {}", refCompany.toString());

        return ResponseEntity.ok(refCompany);
    }
}
