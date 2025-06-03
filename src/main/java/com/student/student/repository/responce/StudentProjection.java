package com.student.student.repository.responce;

public interface StudentProjection {
    String getFirstName();

    String getLastName();

    default String getFullName() {
        return getFirstName() + " " + getLastName();
    }
}
