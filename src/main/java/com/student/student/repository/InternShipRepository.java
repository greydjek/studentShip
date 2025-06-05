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

public interface InternShipRepository extends JpaRepository<InternShip, UUID> {

    @Query(value = """
select i.id as id, i.studentId.firstName as firstName,
 i.studentId.lastName as lastName, i.companyId.name as nameCompany,
 i.startData as startDate, i.endData as endData
from InternShip as i
where i.id = :id
""")
   Optional <InternShipProjection> findByInternshipProjection(@Param("id") UUID id);

    @Query(value = """
            SELECT i.id as id,
             i.studentId.lastName as lastName,
             i.studentId.firstName as firstName,
             i.companyId.name AS companyName,
             i.startData AS startDate,
             i.endData AS endDate,
             i.position AS position,
             i.comments AS comments
            FROM InternShip AS i
            """, countQuery = "SELECT COUNT(i) FROM InternShip i")
    Page<DtoResponseInternShip> findAllRequestDto(Pageable pageable);
}
