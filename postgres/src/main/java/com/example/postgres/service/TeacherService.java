package com.example.postgres.service;

import com.example.postgres.entity.Centers;
import com.example.postgres.entity.Teacher;
import com.example.postgres.repository.CenterRepo;
import com.example.postgres.repository.TeacherRepo;
import com.example.postgres.request.TeacherData;
import com.example.postgres.service.impl.TeacherImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherService implements TeacherImpl {

    @Autowired
    TeacherRepo repo;

    @Autowired
    CenterRepo centerRepo;

    @Override
    public ResponseEntity<String> updateTeacher(Long id, TeacherData teacher1) {

        Teacher teacher = repo.findById(id).get();

        if (teacher == null) {
            return ResponseEntity.notFound().build();
        }




        if(teacher1.getAgreement() != null){
            teacher.setAgreement(teacher1.getAgreement());
        }

        if(teacher1.getName() != null){
            teacher.setName(teacher1.getName());
        }

        if(teacher1.getTrainerTeacherId() != null){
            teacher.setTrainer(repo.findById(teacher1.getTrainerTeacherId()).get());
        }
        if(teacher1.getBirthDate() != null){
            teacher.setBirthdate(teacher1.getBirthDate());
        }

        if(teacher1.getTeacherCode() != null){
            if(!teacher1.getTeacherCode().equals(teacher.getCode())){
                return ResponseEntity.badRequest().body("You are trying to change teacher code to   "+teacher1.getTeacherCode() +" " +"Dont edit teacher code");
            }
            teacher.setCode(teacher1.getTeacherCode());
        }

        if(teacher1.getQualification()!= null){
            teacher.setQualification(teacher1.getQualification());
        }

        if(teacher1.getAddress()!=null){
            teacher.setAddress(teacher1.getAddress());
        }
        if(teacher1.getEmail() != null){
            teacher.setEmail(teacher1.getEmail());
        }

        if(teacher1.getLevel() != null){
            teacher.setLevel(teacher1.getLevel());
        }

        if(teacher1.getMobileNo() != null){
            teacher.setMobile(teacher1.getMobileNo());
        }

        if(teacher1.getRemovedCenters()!=null){
            List<String>removedCenters = teacher1.getRemovedCenters();
            for (String center : removedCenters){
                Optional<Centers> center1 = centerRepo.findByTeacherIdAndName(id,center);
                if(center1.isPresent()){
                    centerRepo.deleteById(center1.get().getId());

                }



            }

        }


        if(teacher1.getCenters()!=null){
            List<String> centerNames = teacher1.getCenters();
            List<Centers> newCenters = new ArrayList<>();


            for (String center : centerNames) {
                Optional<Centers> existingCenter = centerRepo.findByTeacherIdAndName(id, center);


                if (existingCenter.isEmpty()) {
                    Centers center1 = new Centers();
                    center1.setTeacher(teacher);
                    center1.setName(center);
                    newCenters.add(center1);
                }
            }
            teacher.setCentersList(newCenters);
            centerRepo.saveAll(newCenters);
            repo.save(teacher);



        }
        repo.save(teacher);

        return null;
    }



}
