package com.example.postgres.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllTeacherRes {
    public Long id;
    public Integer level;
    public String name;
    public String center;
    public Long code;
}
