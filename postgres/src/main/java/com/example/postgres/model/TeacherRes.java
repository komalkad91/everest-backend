package com.example.postgres.model;

import com.example.postgres.entity.Centers;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeacherRes {
    private Long id;
    private String name;
    private String token;
    private String role;
    private List<Centers> centersList;
}
