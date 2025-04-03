package com.example.postgres.repository;

import com.example.postgres.entity.LevelMarks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelRepo extends JpaRepository<LevelMarks,Long> {
}
