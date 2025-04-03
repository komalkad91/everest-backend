package com.example.postgres.repository;

import com.example.postgres.entity.Centers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CenterRepo extends JpaRepository<Centers,Long> {

    @Query(value = "select ct.name from centers as ct where teacher_id = :teacherId",nativeQuery = true)
    List<String> findByTeacherId(Long teacherId);

    @Query(value="select *from centers where teacher_id = :teacherId",nativeQuery = true)
    List<Centers> findByTeacher(Long teacherId);


    Optional<Centers> findByTeacherIdAndName(Long teacherId,String name);

    @Override
    void deleteById(Long aLong);
}
