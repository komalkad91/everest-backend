package com.example.postgres.entity;

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

    @Column(name = "reg_id")
    private Long regId;

    private Integer level;

    private Boolean certificate;

    private LocalDate birtDate;

    private String standard;

    private boolean isActive;

    private String mobileNo;

    private String email;

    private LocalDate created_at;

    private String address;

    @ManyToOne
    @JoinColumn(name= "center_id")
    private Centers center;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<LevelMarks> levelMarks;



}
