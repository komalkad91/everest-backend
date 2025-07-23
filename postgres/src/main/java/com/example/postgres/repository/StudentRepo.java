package com.example.postgres.repository;

import com.example.postgres.Projection.AllStudentProjection;
import com.example.postgres.Projection.StudentProjectionWithMarks;
import com.example.postgres.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepo extends JpaRepository<Student,Long> {
    List<Student> findByCenterId(Long id);
    Optional<Student> findByRegId(Long regId);

    @Query(value = """
    SELECT 
        ROW_NUMBER() OVER (ORDER BY s.reg_id DESC) AS srNo,
        s.id AS id, 
        COALESCE(s.name, 'NO NAME') AS name, 
        s.reg_id AS regId, 
        s.level AS level, 
        s.birt_date AS birthDate,
        COALESCE(tc.name, 'NOT ASSIGNED') AS teacher 
    FROM student s
    LEFT JOIN centers ct ON ct.id = s.center_id
    LEFT JOIN teachers tc ON tc.id = ct.teacher_id
    ORDER BY s.reg_id DESC
    """,
            countQuery = """
        SELECT COUNT(*) 
        FROM student s
        LEFT JOIN centers ct ON ct.id = s.center_id
        LEFT JOIN teachers tc ON tc.id = ct.teacher_id
    """,
            nativeQuery = true)
    Page<AllStudentProjection> findAllProjected(Pageable pageable);





    @Query(value = """
    SELECT 
        ROW_NUMBER() OVER (ORDER BY s.reg_id DESC) AS srNo,
        s.id AS id, 
        COALESCE(s.name, 'NO NAME') AS name, 
        s.reg_id AS regId, 
        s.level AS level, 
        s.birt_date AS birthDate,
        COALESCE(tc.name, 'NOT ASSIGNED') AS teacher 
    FROM student s
    LEFT JOIN centers ct ON ct.id = s.center_id
    LEFT JOIN teachers tc ON tc.id = ct.teacher_id
    WHERE (
        LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
        LOWER(COALESCE(tc.name, '')) LIKE LOWER(CONCAT('%', :search, '%')) OR
        CAST(s.reg_id AS TEXT) LIKE CONCAT('%', :search, '%')
    )
    ORDER BY s.reg_id DESC
    """,
            countQuery = """
       SELECT COUNT(*) 
       FROM student s
       LEFT JOIN centers ct ON ct.id = s.center_id
       LEFT JOIN teachers tc ON tc.id = ct.teacher_id
       WHERE (
           LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
           LOWER(COALESCE(tc.name, '')) LIKE LOWER(CONCAT('%', :search, '%')) OR
           CAST(s.reg_id AS TEXT) LIKE CONCAT('%', :search, '%')
       )
    """,
            nativeQuery = true)
    Page<AllStudentProjection> findAllProjectedWithSearch(
            @Param("search") String search,
            Pageable pageable
    );

    @Query(value = """
            select count(st.id) from student as st
            join centers ct on ct.id = st.center_id
            join teachers tc on tc.id = ct.teacher_id
            where tc.id = :teacherId
            """,nativeQuery = true)
    long findTotalStudents(    @Param("teacherId")
                               Long teacherId);


    @Query(value = """
    SELECT COUNT(st.id)
    FROM student st
    JOIN centers ct ON ct.id = st.center_id
    JOIN teachers tc ON tc.id = ct.teacher_id
    WHERE tc.id = :teacherId
      AND (
        LOWER(st.name) LIKE LOWER(CONCAT('%', :searchText, '%'))
        OR CAST(st.reg_id AS TEXT) LIKE CONCAT('%', :searchText, '%')
        OR LOWER(tc.name) LIKE LOWER(CONCAT('%', :searchText, '%'))
      )
    """, nativeQuery = true)
    long findTotalStudentsForTeacher(
            @Param("teacherId") Long teacherId,
            @Param("searchText") String searchText
    );
    long count();


    @Query(value = """
    SELECT COUNT(st.id)
    FROM student st
    LEFT JOIN centers ct ON ct.id = st.center_id
    LEFT JOIN teachers tc ON tc.id = ct.teacher_id
    WHERE (
        LOWER(st.name) LIKE LOWER(CONCAT('%', :searchText, '%'))
        OR CAST(st.reg_id AS TEXT) LIKE CONCAT('%', :searchText, '%')
        OR LOWER(tc.name) LIKE LOWER(CONCAT('%', :searchText, '%'))
    )
    """, nativeQuery = true)
    long findTotalStudentsForAdmin(@Param("searchText") String searchText);





    @Query(value = """
    SELECT 
        ROW_NUMBER() OVER (ORDER BY s.reg_id DESC) AS srNo,
        s.id AS id, 
        s.reg_id AS regId, 
        s.name AS name, 
        s.level AS level, 
        s.birt_date AS birthDate,
        t.name AS teacher
    FROM student s
    LEFT JOIN centers c ON c.id = s.center_id
    LEFT JOIN teachers t ON t.id = c.teacher_id
    WHERE t.id = :teacherId
      AND (
        LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(COALESCE(t.name, '')) LIKE LOWER(CONCAT('%', :search, '%'))
        OR CAST(s.reg_id AS TEXT) LIKE CONCAT('%', :search, '%')
      )
    ORDER BY s.reg_id DESC
    """,
            countQuery = """
    SELECT COUNT(*)
    FROM student s
    LEFT JOIN centers c ON c.id = s.center_id
    LEFT JOIN teachers t ON t.id = c.teacher_id
    WHERE t.id = :teacherId
      AND (
        LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(COALESCE(t.name, '')) LIKE LOWER(CONCAT('%', :search, '%'))
        OR CAST(s.reg_id AS TEXT) LIKE CONCAT('%', :search, '%')
      )
    """,
            nativeQuery = true)
    Page<AllStudentProjection> findAllByTeacherAndSearch(
            @Param("teacherId") Long teacherId,
            @Param("search") String search,
            Pageable pageable
    );



    @Query(value = """
    SELECT 
        ROW_NUMBER() OVER (ORDER BY s.reg_id DESC) AS srNo,
        s.id AS id,
        COALESCE(s.name, 'NO NAME') AS name,
        s.reg_id AS regId,
        s.level AS level,
        s.birt_date AS birthDate,
        s.standard AS standard,
        COALESCE(t.name, 'NOT ASSIGNED') AS teacher,
        lm.l_f as lf,
        lm.l_1 AS l1,
        lm.l_2 AS l2,
        lm.l_3 AS l3,
        lm.l_4 AS l4,
        lm.l_5 AS l5,
        lm.l_6 AS l6,
        lm.l_7 AS l7,
        lm.l_8 AS l8
    FROM student s
    LEFT JOIN centers c ON c.id = s.center_id
    LEFT JOIN teachers t ON t.id = c.teacher_id
    LEFT JOIN level lm ON lm.student_id = s.id
    WHERE (:teacherId IS NULL OR t.id = :teacherId)
      AND (
            LOWER(s.name) LIKE LOWER(CONCAT('%', :searchText, '%'))
            OR LOWER(COALESCE(t.name, '')) LIKE LOWER(CONCAT('%', :searchText, '%'))
            OR CAST(s.reg_id AS TEXT) LIKE CONCAT('%', :searchText, '%')
          )
    ORDER BY s.reg_id DESC
    """,
            countQuery = """
        SELECT COUNT(*)
        FROM student s
        LEFT JOIN centers c ON c.id = s.center_id
        LEFT JOIN teachers t ON t.id = c.teacher_id
        WHERE (:teacherId IS NULL OR t.id = :teacherId)
          AND (
                LOWER(s.name) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(COALESCE(t.name, '')) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR CAST(s.reg_id AS TEXT) LIKE CONCAT('%', :searchText, '%')
              )
    """,
            nativeQuery = true)
    Page<StudentProjectionWithMarks> findForDownloadByTeacher(@Param("teacherId") Long teacherId,
                                                              @Param("searchText") String searchText, Pageable pageable);


    @Query(value = """
    SELECT 
        ROW_NUMBER() OVER (ORDER BY s.reg_id DESC) AS srNo,
        s.id AS id,
        COALESCE(s.name, 'NO NAME') AS name,
        s.reg_id AS regId,
        s.level AS level,
        s.birt_date AS birthDate,
        s.standard AS standard,
        COALESCE(t.name, 'NOT ASSIGNED') AS teacher,
        lm.l_f as lf,
        lm.l_1 AS l1,
        lm.l_2 AS l2,
        lm.l_3 AS l3,
        lm.l_4 AS l4,
        lm.l_5 AS l5,
        lm.l_6 AS l6,
        lm.l_7 AS l7,
        lm.l_8 AS l8
    FROM student s
    LEFT JOIN centers c ON c.id = s.center_id
    LEFT JOIN teachers t ON t.id = c.teacher_id
    LEFT JOIN level lm ON lm.student_id = s.id
    WHERE 
        LOWER(s.name) LIKE LOWER(CONCAT('%', :searchText, '%'))
        OR LOWER(COALESCE(t.name, '')) LIKE LOWER(CONCAT('%', :searchText, '%'))
        OR CAST(s.reg_id AS TEXT) LIKE CONCAT('%', :searchText, '%')
    ORDER BY s.reg_id DESC
    """,
            countQuery = """
        SELECT COUNT(*)
        FROM student s
        LEFT JOIN centers c ON c.id = s.center_id
        LEFT JOIN teachers t ON t.id = c.teacher_id
        WHERE 
            LOWER(s.name) LIKE LOWER(CONCAT('%', :searchText, '%'))
            OR LOWER(COALESCE(t.name, '')) LIKE LOWER(CONCAT('%', :searchText, '%'))
            OR CAST(s.reg_id AS TEXT) LIKE CONCAT('%', :searchText, '%')
    """,
            nativeQuery = true)
    Page<StudentProjectionWithMarks> findForAdminDownload(@Param("searchText") String searchText, Pageable pageable);






}
