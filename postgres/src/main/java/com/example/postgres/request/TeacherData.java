package com.example.postgres.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class TeacherData {
    private long id;
    private String userName;
    private String email;
    private String mobileNo;
    private String address;
    private Long trainerTeacherId;
    private String trainerTeacherName;
    private String city;
    private Boolean agreement;
    private String pin;
    private LocalDate birthDate;
    private String qualification;
    private Integer level;
    private String name;
    private List<String> centers;
    private List<String> removedCenters;
    private Long teacherCode;
    private String password;
    private Long studentCount;


}
