package com.student.student.controller;

import com.student.student.data.Company;
import com.student.student.responce.company.CompanyProjectionAndId;
import com.student.student.responce.company.CompanyResponceRecord;
import com.student.student.service.CompanyService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/")
    public ResponseEntity<Company> createNewCompany(@RequestBody Company company) {
        log.info("принят объект {}", company);
        return companyService.saveNewCompany(company);
    }

    @GetMapping("/all")
    public Page<CompanyProjectionAndId> findAll(@PageableDefault(size = 11, sort = "name") Pageable pageable) {
        return companyService.findAll(pageable);
    }

    //Todo рассмотреть вариант soft delete
    @DeleteMapping("/")
    public ResponseEntity<?> delete(@RequestParam UUID id) {
        return companyService.deleteById(id);
    }

    @PutMapping("/")
    public ResponseEntity<Company> refactorCompany(@RequestBody Company company) {
        log.info("принят объект для обновления компании {}", company);
        return companyService.refactor(company);
    }

    @GetMapping("/")
    public ResponseEntity<String> findByName(@RequestParam String companyName) {
        log.info("контроллер принял имя компании {} ", companyName);
        return companyService.findByName(companyName);
    }
}
