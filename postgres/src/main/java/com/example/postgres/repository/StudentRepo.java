package com.example.postgres.repository;

import com.example.postgres.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepo extends JpaRepository<Student,Long> {
    List<Student> findByCenterId(Long id);
    Optional<Student> findByRegId(Long regId);






}
