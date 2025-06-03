package com.student.student.repository.responce;

import java.util.UUID;

public record StudentRecordResponse(UUID uuid, String firstName, String lastName) {
}
