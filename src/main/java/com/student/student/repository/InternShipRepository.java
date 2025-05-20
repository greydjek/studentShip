package com.student.student.repository;

import com.student.student.data.InternShip;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternShipRepository extends JpaRepository<InternShip, UUID> {

}
