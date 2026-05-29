package com.example.postgres.repository;

import com.example.postgres.entity.LevelMarks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LevelRepo extends JpaRepository<LevelMarks,Long> {
    
    /**
     * Find LevelMarks by student ID
     * Used to prevent duplicate key violations when student-levelMarks relationship is broken
     */
    Optional<LevelMarks> findByStudentId(Long studentId);
}
