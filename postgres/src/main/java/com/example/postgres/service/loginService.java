package com.example.postgres.service;

import com.example.postgres.entity.Teacher;
import com.example.postgres.exception.DataNotFound;
import com.example.postgres.repository.TeacherRepo;
import com.example.postgres.request.CreatePass;
import com.example.postgres.request.login;
import com.example.postgres.service.impl.loginImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class loginService implements loginImpl {

    @Autowired
    public TeacherRepo teacherRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void  savePassword() {
        List<Teacher> teachers = teacherRepo.findAll();
        for (Teacher teacher : teachers) {

            if (teacher.getName() != null && teacher.getCode() != null && teacher.getCode() >735) {
                String rawPassword = teacher.getPassword();
                String encodedPassword = passwordEncoder.encode(rawPassword);

                teacher.setUsername("TEACHER" + teacher.getCode());
                teacher.setPassword(encodedPassword);
                teacher.setPasswordCreated(true);

                teacherRepo.save(teacher);
            }
        }


//
//        Teacher teacher1 = checkIfTeacherExist(log);
//           String decodedPassword = passwordEncoder.encode(log.getPassword());
//           teacher1.setUsername(log.getUsername());
//           teacher1.setPassword(decodedPassword);
//           teacher1.setPasswordCreated(true);
//           teacherRepo.save(teacher1);



    }

    public ResponseEntity<String> logInUSer(login log){
      Teacher teacher1 = checkIfTeacherExistByUsername(log);

        if(!teacher1.isPasswordCreated()){
            throw new DataNotFound("Please Create Password first");

        }

        if(!teacher1.getPassword().equals(log.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password not matched");
        }
        return ResponseEntity.ok("success");


    }


    public Teacher checkIfTeacherExistByUsername(login passData){
        Teacher teacher1 = new Teacher();


        teacher1 = teacherRepo.findByUsername(passData.getUsername());
        if (teacher1 == null) {
            throw new DataNotFound("Teacher not exist");
        };
        return teacher1;

    }


    public Teacher checkIfTeacherExist(CreatePass passData){
        Teacher teacher1 = new Teacher();

        try{
            teacher1 = teacherRepo.findByUsername(passData.getUsername());


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (teacher1 == null) {
            throw new DataNotFound("Teacher not exist");
        };
        return teacher1;

    }
}
