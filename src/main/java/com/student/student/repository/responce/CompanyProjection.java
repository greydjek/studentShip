package com.student.student.repository.responce;


import java.util.UUID;
import org.apache.commons.text.translate.UnicodeUnescaper;

public interface CompanyProjection {
    UUID getId();

    String getName();

    String getAddress();

    default String getCompanyProjection() {
        return getName() + " расположенная по адресу " + getAddress();
    }
}
