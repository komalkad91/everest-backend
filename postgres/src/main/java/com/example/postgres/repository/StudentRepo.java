package com.example.postgres.repository;

import com.example.postgres.Projection.AllStudentProjection;
import com.example.postgres.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepo extends JpaRepository<Student,Long> {
    List<Student> findByCenterId(Long id);
    Optional<Student> findByRegId(Long regId);

    @Query("SELECT s.id AS id, s.name AS name, s.regId AS regId, s.level AS level FROM Student s")
    Page<AllStudentProjection> findAllProjected(Pageable pageable);






}
