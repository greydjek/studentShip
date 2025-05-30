package com.student.student.controller;

import com.student.student.data.Company;
import com.student.student.repository.responce.CompanyProjection;
import com.student.student.service.CompanyService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/com")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/new")
    public ResponseEntity createNewCompany(@RequestBody Company company) {
        return companyService.createNewCompany(company);
    }

    @GetMapping("/all")
    public List<Company> findAll() {
        return companyService.findAll();
    }

    @DeleteMapping("/delete")
    public HttpStatus delete(@RequestParam String id) {
        return companyService.deleteById(id);
    }

    @GetMapping("/find")
    public ResponseEntity<String> findByName(@RequestParam String companyName) {
        log.info("контроллер принял имя компании {} ", companyName);
        return companyService.findByName(companyName);
    }
}
