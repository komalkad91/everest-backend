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

    @Query(value = """
    SELECT 
        s.id AS id, 
        COALESCE(s.name,'NO NAME') AS name, 
        s.reg_id AS regId, 
        s.level AS level, 
        COALESCE(tc.name,'NOT ASSIGNED') AS teacher 
    FROM student s
    LEFT JOIN centers ct ON ct.id = s.center_id
    LEFT JOIN teachers tc ON tc.id = ct.teacher_id
    """,
            nativeQuery = true)

    Page<AllStudentProjection> findAllProjected(Pageable pageable);


    @Query(value = """
            select count(st.id) from student as st
            join centers ct on ct.id = st.center_id
            join teachers tc on tc.id = ct.teacher_id
            where tc.id = :teacherId
            """,nativeQuery = true)
    long findTotalStudents(Long teacherId);

    long count();



//    select count(st.id) from student as st
//    join centers ct on ct.id = st.center_id
//    join teachers tc on tc.id = ct.teacher_id
//    where tc.id = 1






}
