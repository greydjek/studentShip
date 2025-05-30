package com.student.student.repository;

import com.student.student.data.Student;
import com.student.student.repository.responce.StudentProjection;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
 Stream<StudentProjection> findAllProjectedBy();
@Query(value = """
        select s.firstName, s.lastName
        from Student s
        """)
    ResponseEntity<StudentProjection> findByIdProjection(UUID id);
}
