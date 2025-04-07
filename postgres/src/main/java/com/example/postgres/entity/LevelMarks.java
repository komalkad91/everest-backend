package com.example.postgres.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name="level")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LevelMarks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer level;

    private Integer marks;

    private Integer l_f;

    private Integer l_1;

    private Integer l_2;

    private Integer l_3;

    private Integer l_4;

    private Integer l_5;

    private Integer l_6;

    private Integer l_7;

    private Integer l_8;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    private Student student;


}
