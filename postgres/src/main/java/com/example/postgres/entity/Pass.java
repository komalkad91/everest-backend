package com.example.postgres.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name="pass")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pass {
    @Id
    private Long code;
    private String password;
}
