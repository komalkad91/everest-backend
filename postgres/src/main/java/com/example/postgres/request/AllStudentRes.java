package com.example.postgres.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllStudentRes {
    private Long id;
    private Long reg_id;
    private Integer level;
    private String name;
}
