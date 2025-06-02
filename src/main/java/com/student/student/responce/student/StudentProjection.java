package com.student.student.responce.student;

public interface StudentProjection {
    String getFirstName();

    String getLastName();

    default String getFullName() {
        return getFirstName() + " " + getLastName();
    }
}
