package com.student.student.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class InternShip {

    @Id
    UUID id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    Student studentId;

    @ManyToOne
    @JoinColumn(name = "company_id")
    Company companyId;

    @Column(name = "start_date")
    LocalDate startData;

    @Column(name = "end_date")
    LocalDate andData;

    @Column(name = "position")
    String position;

    @Column(name = "comments_works")
    String comments;

    @Column(name = "created_at", insertable = false, updatable = false)
    LocalDate localDate;
}
