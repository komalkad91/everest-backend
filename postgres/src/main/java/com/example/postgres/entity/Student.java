package com.example.postgres.entity;

import com.example.postgres.enums.Type;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="Student")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Column(name = "reg_id", unique = true, updatable = false)
    private Long regId;

    private Integer level;

    private String certificate;


    private String birtDate;

    private String standard;

    private String isActive;

    private String mobileNo;

    private String email;

    private LocalDate created_at;

    private String address;

    @ManyToOne
    @JoinColumn(name= "center_id")
    private Centers center;

    private Type type;

    @OneToOne(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private LevelMarks levelMarks;



}
