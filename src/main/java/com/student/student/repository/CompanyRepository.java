package com.student.student.repository;

import com.student.student.data.Company;
import com.student.student.responce.company.CompanyProjection;
import com.student.student.responce.company.CompanyProjectionAndId;
import com.student.student.responce.company.CompanyResponceRecord;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    @Query(value = """ 
            SELECT c.id AS id, c.name AS name, c.address AS address
            FROM Company AS c
            WHERE LOWER(c.name)= LOWER(:name)
            """)
    Optional<CompanyProjection> findByName(@Param("name") String name);

    @Query(value = """
            SELECT c.id AS id, c.name AS name, c.address AS address
            FROM Company AS c
            WHERE id= :id
            """)
    Optional<CompanyProjection> findByIdProjection(@Param("id") UUID id);

    @Query(value = """
            SELECT
             c.id AS id,
             c.name AS name,
             c.address AS address
            FROM Company AS c
            """, countQuery = "SELECT COUNT(c) FROM Company AS c")
    Page<CompanyProjectionAndId> findAllForProjection(Pageable pageable);

    @Query(value = """
                SELECT
                    c.id AS id,
                    c.name AS name,
                    c.address AS address
                FROM Company c
                WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))
            """, countQuery = "SELECT COUNT(c) FROM Company c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<CompanyProjectionAndId> findLikeNameCompany(@Param("name") String name, Pageable pageable);
}
