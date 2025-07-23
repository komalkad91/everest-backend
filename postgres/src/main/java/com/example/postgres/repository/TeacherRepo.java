package com.example.postgres.repository;
 import com.example.postgres.entity.Teacher;
 import com.example.postgres.model.TeacherRes;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.data.jpa.repository.Query;
 import org.springframework.data.repository.query.Param;

 import java.util.List;
 import java.util.Optional;

public interface TeacherRepo extends JpaRepository<Teacher,Long> {
 Teacher findByEmail(String email);

 Teacher findByUsername(String userName);



 List<Teacher> findAll();

 @Query(value = """
    SELECT COUNT(s.id)
    FROM student s
    LEFT JOIN centers c ON c.id = s.center_id
    WHERE c.teacher_id = :teacherId
    """, nativeQuery = true)
 Long countByTeacher(@Param("teacherId") Long teacherId);

 boolean existsByCode(Long code);









}


