package com.student.student.responce;

import java.util.UUID;

public interface StudentFullProjection {
    UUID getUuid();

    String getFullName();

    String getSpecialization();

    Integer getCourse();
}
