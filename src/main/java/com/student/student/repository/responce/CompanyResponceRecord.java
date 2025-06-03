package com.student.student.repository.responce;

import com.student.student.data.Company;
import java.util.UUID;

public record CompanyResponceRecord(UUID id, String name, String address) {
}
