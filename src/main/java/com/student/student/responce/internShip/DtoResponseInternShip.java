package com.student.student.responce.internShip;

import java.time.LocalDate;
import java.util.UUID;

public interface DtoResponseInternShip {
    UUID getId();

    UUID getStudentId();

    UUID getCompanyId();

    String getLastName();

    String getFirstName();

    String getCompanyName();

    LocalDate getStartDate();

    LocalDate getEndDate();

    String getPosition();

    String getComments();

    default String studentFullName() {
        return getFirstName() + getLastName();
    }
}
