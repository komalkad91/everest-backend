package com.example.postgres.service.impl;

import com.example.postgres.request.TeacherData;
import org.springframework.http.ResponseEntity;

public interface TeacherImpl {
    public ResponseEntity<String> updateTeacher(Long id, TeacherData teacher1);
}
