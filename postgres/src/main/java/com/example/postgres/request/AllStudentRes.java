package com.example.postgres.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllStudentRes {
    private Long id;
    private Long regId;
    private Integer level;
    private String birthDate;
    private String teacher;
    private String name;
}
