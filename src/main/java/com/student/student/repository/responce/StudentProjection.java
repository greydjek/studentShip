package com.student.student.repository.responce;

import java.util.Optional;

public interface StudentProjection {
    String getFirstName();

    String getLastName();

    default String getFullName() {
        return getFirstName() + " " + getLastName();

    }
}
