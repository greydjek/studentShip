package com.student.student.service;

import com.student.student.data.Company;
import com.student.student.exeption.ErrorMessage;
import com.student.student.exeption.MyExceptionHandler;
import com.student.student.repository.CompanyRepository;
import com.student.student.repository.responce.CompanyProjection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private static final Logger log = LoggerFactory.getLogger(CompanyService.class);
    private final CompanyRepository companyRepository;

    //todo проверка данных полей
    public ResponseEntity createNewCompany(Company company) {
        companyRepository.save(company);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public ResponseEntity<String> findByName(String name) {
        return companyRepository.findByName(name).map( CompanyProjection::getCompanyProjection)
                .map(
                companyProjection -> {
                    log.info("нашли компанию по имени {}", companyProjection);
                    return ResponseEntity.ok(companyProjection);
                }).orElseThrow(() ->
        {
            log.info("не нашли компанию по имени проверьте инф");
            throw new MyExceptionHandler(HttpStatus.NOT_FOUND, ErrorMessage.COMPANY_NOT_FOUND);
        });
    }

    public HttpStatus deleteById(String id) {
        UUID uuid = UUID.fromString(id);
        Optional<Company> company = companyRepository.findById(uuid);
        if (company.isPresent()) {
            companyRepository.deleteById(uuid);
            return HttpStatus.OK;
        } else return HttpStatus.NOT_FOUND;
    }
}
