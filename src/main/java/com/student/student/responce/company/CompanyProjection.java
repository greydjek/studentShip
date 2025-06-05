package com.student.student.responce.company;


import java.util.UUID;

public interface CompanyProjection {
    UUID getId();

    String getName();

    String getAddress();

    default String getCompanyProjection() {
        return getName() + " расположенная по адресу " + getAddress();
    }
}
