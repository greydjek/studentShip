package com.student.student.controller;

import com.student.student.data.InternShip;
import com.student.student.request.RequestDtoInternShip;
import com.student.student.responce.internShip.DtoResponseInternShip;
import com.student.student.responce.internShip.InternShipProjection;
import com.student.student.service.InternshipService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping("/internship")
public class InternshipController {

    private final InternshipService internShipService;

    @GetMapping("/{id}")
    public ResponseEntity<InternShipProjection> getInternShipProjection(@PathVariable("id") UUID uuid) {
        return internShipService.getInternShipFromId(uuid);
    }

    @PutMapping("/")
    public ResponseEntity<?> refactorInternShip(@RequestBody RequestDtoInternShip requestDtoInternShip) {
        return internShipService.refactorInternShip(requestDtoInternShip);
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteInternShip(@RequestParam UUID uuid) {
        return internShipService.deleteById(uuid);
    }

    @GetMapping("/")
    public Page<DtoResponseInternShip> getAllInternShip(@PageableDefault(size = 18, sort = "startDate") Pageable pageable){
        return internShipService.getAllInternShip(pageable);
    }

}
