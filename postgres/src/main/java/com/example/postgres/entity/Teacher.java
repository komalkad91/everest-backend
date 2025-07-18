package com.example.postgres.entity;


import com.example.postgres.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="teachers")
@Builder
@Entity
public class Teacher {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;


    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "role")
    private Role roles = Role.TEACHER;

    @Column(name = "email")
    private String email;

    @Column(name = "password",nullable = true)
    private String password;

    @Column(name="name",nullable = false)
    private String name;

    @Column(name = "is_password_created",nullable = true)
    private boolean isPasswordCreated;

    private String mobile;

    @Column(name = "address")
    private String address;

    @Column(name = "agreement")
    private Boolean agreement;

    @ManyToOne
    @JoinColumn(name="trainer_teacher_id")
    @JsonIgnore
    private Teacher trainer;

    @Column(name = "pin")
    private String pin;

    @Column(name = "qualification")
    private String qualification;

//    @Column(name = "birth_date")
//    private LocalDate birthDate;


    @Column(name = "level")
    private Integer level;


    private String centers;

    private Long code;

    @Column(name="birthdate")
    private LocalDate birthdate;

    @OneToMany(mappedBy = "teacher",fetch = FetchType.LAZY)
    private List<Centers>centersList;

    @Column(name = "is_logged_in")
    private Boolean isLoggedIn = false;


    @JsonProperty("trainerId")
   public Long getTrainerTeacherId(){
        return (trainer!=null) ? trainer.getId() : null;
    }


    @JsonProperty("trainerName")
    public String getTrainerName(){
        return (trainer!=null) ? trainer.getName() : null;

    }

    @JsonProperty("defaultCenter")
    public String getDefaultCenter(){
        if(centersList==null || centersList.isEmpty()){
            return "No Center";
        }
       return  centersList.get(0).getName();
    }







}