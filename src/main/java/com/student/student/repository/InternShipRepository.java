package com.student.student.repository;

import com.student.student.data.InternShip;
import com.student.student.responce.internShip.DtoResponseInternShip;
import com.student.student.responce.internShip.InternShipProjection;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InternShipRepository extends JpaRepository<InternShip, UUID> {

    @Query(value = """
            SELECT i.id AS id,
             i.studentId.firstName AS firstName,
             i.studentId.lastName AS lastName,
             i.companyId.name AS nameCompany,
             i.startData AS startDate,
             i.endData AS endData
            FROM InternShip AS i
            WHERE i.id = :id
            """)
   Optional <InternShipProjection> findByInternshipProjection(@Param("id") UUID id);

    @Query(value = """
            SELECT i.id AS id,
             i.studentId.lastName AS lastName,
             i.studentId.firstName AS firstName,
             i.studentId.id AS studentId,
             i.companyId.id AS companyId,
             i.companyId.name AS companyName,
             i.startData AS startDate,
             i.endData AS endDate,
             i.position AS position,
             i.comments AS comments
            FROM InternShip AS i
            """, countQuery = "SELECT COUNT(i) FROM InternShip i")
    Page<DtoResponseInternShip> findAllRequestDto(Pageable pageable);

    @Query(value = """
            SELECT i.id AS id,
             i.studentId.lastName AS lastName,
             i.studentId.firstName AS firstName,
             i.studentId.id AS studentId,
             i.companyId.id AS companyId,
             i.companyId.name AS companyName,
             i.startData AS startDate,
             i.endData AS endDate,
             i.position AS position,
             i.comments AS comments
            FROM InternShip AS i
            WHERE LOWER(i.studentId.lastName)  LIKE LOWER(CONCAT('%', :likeName, '%'))
            """, countQuery = "SELECT COUNT(i) FROM InternShip i")
    Page<DtoResponseInternShip> findAllRequestDtoForLikeStudentName(@Param("likeName") String likeName, Pageable pageable);

    @Query("""
            SELECT i.id AS id,
             i.studentId.lastName AS lastName,
             i.studentId.firstName AS firstName,
             i.studentId.id AS studentId,
             i.companyId.id AS companyId,
             i.companyId.name AS companyName,
             i.startData AS startDate,
             i.endData AS endDate,
             i.position AS position,
             i.comments AS comments
             FROM InternShip AS i
            WHERE i.id = :internshipId
            """)
    DtoResponseInternShip findByIdDtoResponseInternship(@Param("internshipId") UUID internshipId);

@Query("""
SELECT i.comments AS comments
FROM InternShip i
WHERE i.id = :id
""")
    String getCommentsById(@Param("id") UUID id);
}
