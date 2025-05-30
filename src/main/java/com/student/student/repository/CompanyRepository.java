package com.student.student.repository;

import com.student.student.data.Company;
import com.student.student.repository.responce.CompanyProjection;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    @Query(value = """ 
SELECT c.name AS name, c.address AS address
FROM Company as c
WHERE LOWER(c.name)= LOWER(:name)
""")
    Optional<CompanyProjection> findByName(@Param("name") String name);
}
