package com.student.student.data;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "student")
public class Student {

    @Id
    @Column(name = "id")
    UUID id;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "middle_name")
    String middleName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "mobile_phone")
    Long specialization;

    @Column(name = "cource")
    Integer cource;

    @Column(name = "worker")
    String worker;

    @Column(name = "creat_at")
    LocalDate localDate;

    @OneToMany(mappedBy = "studentId", cascade = CascadeType.ALL)
    List<InternShip> internShips = new ArrayList<>();
}
