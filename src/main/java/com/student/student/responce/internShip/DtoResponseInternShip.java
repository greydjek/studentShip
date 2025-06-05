package com.student.student.responce.internShip;

import java.time.LocalDate;
import java.util.UUID;

public interface DtoResponseInternShip {
        UUID getId();
        String getLastName();
        String getFirstName();
        String getCompanyName();
        LocalDate getStartDate();
        LocalDate getEndDate();
        String getPosition();
        String getComments();
}
