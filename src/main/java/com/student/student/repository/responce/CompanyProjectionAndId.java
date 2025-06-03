package com.student.student.repository.responce;

import java.util.UUID;

public interface CompanyProjectionAndId {
    UUID getId();

    String getName();

    String getAddress();

    public default String getCompanyNameAddressAndId() {
        return getName() + " находиться по адресу " + getAddress();
    }
}
