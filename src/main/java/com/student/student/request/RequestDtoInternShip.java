package com.student.student.request;

import java.time.LocalDate;
import java.util.UUID;

public record RequestDtoInternShip(
        UUID id,
        UUID studentId,
        UUID companyId,
        LocalDate startDate,
        LocalDate endDate,
        String position,
        String comments) {
}
