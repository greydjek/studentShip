package com.student.student.repository.responce;


public interface CompanyProjection {
    String getName();

    String getAddress();

    default String getCompanyProjection() {
        return getName() + " расположенная по адресу " + getAddress();
    }
}
