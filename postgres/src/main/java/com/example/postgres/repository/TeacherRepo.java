package com.example.postgres.repository;
 import com.example.postgres.entity.Teacher;
 import com.example.postgres.model.TeacherRes;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.data.jpa.repository.Query;

 import java.util.List;
 import java.util.Optional;

public interface TeacherRepo extends JpaRepository<Teacher,Long> {
 Teacher findByEmail(String email);

 Teacher findByUsername(String userName);



 @Query(value = "Select t.id,t.name from teachers t",nativeQuery = true)
 List<Object[]>findAllTeacher();





}


