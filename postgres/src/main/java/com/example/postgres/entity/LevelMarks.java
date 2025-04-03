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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Student student;


}
