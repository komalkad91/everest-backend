package com.example.postgres.repository;

import com.example.postgres.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepo extends JpaRepository<Request,Long> {

    List<Request> findByTeacherId(Long teacherId);

    List<Request> findAll();


}
