package com.student.student.responce.internShip;

import java.time.LocalDate;
import java.util.UUID;

public interface InternShipProjection {

    UUID getId();

    String getLastName();

    String getFirstName();

    String getNameCompany();

    LocalDate getStartDate();

    LocalDate getEndDate();

}
