package com.example.postgres;


import com.example.postgres.entity.Centers;
import com.example.postgres.entity.Teacher;
import com.example.postgres.exception.DataNotFound;
import com.example.postgres.model.AllTeacherRes;
import com.example.postgres.model.TeacherRes;
import com.example.postgres.repository.CenterRepo;
import com.example.postgres.repository.TeacherRepo;
import com.example.postgres.request.CreatePass;
import com.example.postgres.request.UserNameData;
import com.example.postgres.request.login;
import com.example.postgres.request.TeacherData;
import com.example.postgres.security.jwt.JwtTokenUtil;
import com.example.postgres.service.TeacherService;
import com.example.postgres.service.loginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://everest-abacus.cloudjiffy.net"}, allowedHeaders = "*")
public class TeacherController {

    @Autowired
    TeacherRepo repo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private loginService LoginService;

    @Autowired
    private TeacherService teacherService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    CenterRepo centerRepo;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @PostMapping("/addTeacher")
    public void addTeacher(@RequestBody Teacher teacher) {
        repo.save(teacher);
    }

    @PostMapping("/createPassword")
    @CrossOrigin({"http://localhost:3000", "https://everst-abacus.cloudjiffy.net/"})
    public ResponseEntity<String> createPassword(@RequestBody CreatePass log) throws DataNotFound{

            LoginService.savePassword(log);
            return  ResponseEntity.ok("done");

    }

    @GetMapping("/getAllteacher")
    public List<AllTeacherRes> getAllteacher(){
        List<AllTeacherRes>teacherResList = new ArrayList<>();
        repo.findAll().forEach(obj->{
           AllTeacherRes teacherRes = new AllTeacherRes();
           teacherRes.setCode(obj.getCode());
           teacherRes.setId(obj.getId());
           teacherRes.setName(obj.getName());
           teacherRes.setCenter(obj.getDefaultCenter());
           teacherResList.add(teacherRes);
       });
       return teacherResList;
    }

    @GetMapping("/getTeacher")
    public TeacherData getTeacher(@RequestParam Long id){
        Teacher teacher2= repo.findById(id).get();
        List<String>centersList =centerRepo.findByTeacherId(id);

        TeacherData teacher1= TeacherData.builder().address(teacher2.getAddress()).email(teacher2.getEmail()).userName(teacher2.getUsername())
                .trainerTeacherId(teacher2.getTrainerTeacherId()).pin(teacher2.getPin()).birthDate(teacher2.getBirthdate()).qualification(teacher2.getQualification()).
                id(teacher2.getId()).teacherCode(teacher2.getCode()).
                level(teacher2.getLevel()).mobileNo(teacher2.getMobile()).name(teacher2.getName()).trainerTeacherName(teacher2.getTrainerName()).
                centers(centersList).build();

        return teacher1;
    }




    @PostMapping("/login")
    public ResponseEntity<TeacherRes>login(@RequestBody login log){

        TeacherRes teacherRes = new TeacherRes();
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(log.getUsername(), log.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            User userDetails = (User) authentication.getPrincipal();
            Teacher person = repo.findByUsername(log.getUsername());
            String accessToken = "";

            if(person != null){

              accessToken = jwtTokenUtil.generateAccessToken(person);
              String refreshToken = jwtTokenUtil.generateRefreshToken(person);


            }


            teacherRes.setName(person.getName());
            teacherRes.setToken(accessToken);
            teacherRes.setId(person.getId());
            teacherRes.setCentersList(centerRepo.findByTeacher(person.getId()));
            teacherRes.setRole(person.getRoles().name());


            return ResponseEntity.ok(teacherRes);
        } catch (AuthenticationException e) {
            teacherRes.setToken(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(teacherRes);
        }



    }





    @PostMapping("/addNewTeacher")
    public String addNewTeacher(@RequestBody TeacherData teacher1){
        Teacher teacher2 = new Teacher();
        teacher2.setAddress(teacher1.getAddress());
        teacher2.setBirthdate(teacher1.getBirthDate());
        teacher2.setTrainer(repo.findById(teacher1.getTrainerTeacherId()).get());
        teacher2.setMobile(teacher1.getMobileNo());
        teacher2.setLevel(teacher1.getLevel());
        teacher2.setName(teacher1.getName());
        teacher2.setQualification(teacher1.getQualification());
        teacher2.setEmail(teacher1.getEmail());
        teacher2.setPin(teacher1.getPin());

        Teacher teacher3 =repo.save(teacher2);

        List<Centers> centers = (teacher1.getCenters()).stream()
                .map(centerName -> {
                    Centers center = new Centers();
                    center.setName(centerName);

                    center.setTeacher(teacher3);
                    centerRepo.save(center);
                    return center;
                })
                .collect(Collectors.toList());
        teacher3.setCentersList(centers);
        repo.save(teacher3);

        ;
        centerRepo.saveAll(centers);


        return "Success";


    }


    @PostMapping("/editTeacher")
    public  String editTeacher(@RequestBody TeacherData teacher1){

        teacherService.updateTeacher(teacher1.getId(), teacher1);


        return "Success";


    }



    @PostMapping("/removeCenter")
    public ResponseEntity<String> removeCenter(@RequestParam Long teacherId,@RequestBody String center){
       Optional<Centers> centers = centerRepo.findByTeacherIdAndName(teacherId,center);
       if(centers.isPresent()){
           centerRepo.deleteById(centers.get().getId());
           return ResponseEntity.ok("Center Remove from db");

       }else{
           return ResponseEntity.ok("Center not present in database");
       }


    }



}

