package com.example.postgres.request;

import com.example.postgres.entity.Centers;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class StudentData {

    private Long id;
     private String birtDate;

    private Long regId;

    private String name;
    private String mobileNo;
    private Integer level;

    private String address;
    private Long centerId;
    private Boolean isActive;
    private String standard;
    private String email;
    private Integer marks;
    private List<Integer>levelMarks;
    private Long assignTeacher;





}
