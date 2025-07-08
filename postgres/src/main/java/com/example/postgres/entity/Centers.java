package com.example.postgres.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "centers")
public class Centers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name="teacherId")
    @ManyToOne
    @JsonIgnore
    private Teacher teacher;

    private String name;


    @OneToMany(mappedBy = "center", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Student> studentList;

//    public Long getTeacherId(){
//        return teacher.getId();
//    }

}
