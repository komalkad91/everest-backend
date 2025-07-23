package com.example.postgres.repository;

import com.example.postgres.entity.Pass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepo extends JpaRepository<Pass,Long> {
}
