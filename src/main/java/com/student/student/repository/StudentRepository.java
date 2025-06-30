package com.student.student.repository;

import com.student.student.data.Student;
import com.student.student.responce.StudentFullProjection;
import com.student.student.responce.student.StudentProjection;
import com.student.student.responce.student.StudentProjectionAndId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    @Query(value = """
            select s.firstName, s.lastName
            from Student s
            """)
    ResponseEntity<StudentProjection> findByIdProjection(UUID id);

    @Modifying
    @Query(value = """
            UPDATE Student s
            SET s.course = s.course + 1
            WHERE s.course < 5""")
    void incrementCourses();

    @Query(value = """
            SELECT s.id as uuid, s.firstName as firstName, s.lastName as lastName
            FROM Student as s
            WHERE LOWER(s.lastName)  LIKE LOWER(CONCAT('%', :likeName, '%'))
            """)
    List<StudentProjectionAndId> findByLikeNameStudent(@Param("likeName") String likeName);

    @Query(value = """
            select s.id as uuid, (s.firstName, s.lastName) as fullName, s.specialization as specialization,
            s.course as course
            from Student as s
            where LOWER(s.lastName)  LIKE LOWER(CONCAT('%', :likeName, '%'))
            order by s.lastName asc
            """)
    List<StudentFullProjection> findByLikeNameFullProjectionStudent(@Param("likeName") String likeName);

    @Query(value = """
            select s.id as uuid, s.firstName as firstName, s.lastName as lastName
            from Student as s
            order by firstName asc
            """)
    List<StudentProjectionAndId> findAllProjectionForFront();

    @Query(value = """
            select s.id as uuid, s.firstName as firstName, s.lastName as lastName
            from Student as s
            order by firstName asc
            """, countQuery = "SELECT COUNT(s) FROM Student AS s")
    Page<StudentProjectionAndId> findAllForProjection(Pageable page);

    @Query(value = """
            select s.id as uuid, (s.firstName ||" "|| s.lastName) as fullName, s.specialization as specialization,
            s.course as course
            from Student as s
            where s.id = :uuid
            """)
    Optional<StudentFullProjection> findByIdFullProjection(@Param("uuid") UUID uuid);

    @Query(value = """
            select s.id as uuid, (s.firstName ||" "|| s.lastName ||" "|| s.middleName) as fullName, s.specialization as specialization,
            s.course as course
            from Student as s
            order by s.lastName asc
            """, countQuery = "SELECT COUNT(s) FROM Student AS s")
    Page<StudentFullProjection> findAllForMainPage(Pageable page);
}
