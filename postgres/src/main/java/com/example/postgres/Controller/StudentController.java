package com.example.postgres.Controller;

import com.example.postgres.entity.Centers;
import com.example.postgres.entity.LevelMarks;
import com.example.postgres.entity.Student;
import com.example.postgres.repository.CenterRepo;
import com.example.postgres.repository.LevelRepo;
import com.example.postgres.repository.StudentRepo;
import com.example.postgres.request.AllStudentRes;
import com.example.postgres.request.StudentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://everest-abacus.cloudjiffy.net/"})
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    CenterRepo centerRepo;

    @Autowired
    StudentRepo studentRepo;

    @Autowired
    LevelRepo levelRepo;



    @PostMapping("/add/{teacherId}")
    public Student addStudent(@PathVariable Long teacherId, @RequestBody StudentData studentData){
        Student student1;
        if(studentData.getId() != null){


            Student existingStudent = studentRepo.findById(studentData.getId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            student1 = Student.builder()
                    .id(existingStudent.getId())  // Keep the same ID
                    .regId(studentData.getRegId())
                    .birtDate(studentData.getBirtDate())
                    .standard(studentData.getStandard())
                    .level(studentData.getLevel())
                    .name(studentData.getName())
                    .address(studentData.getAddress())
                    .mobileNo(studentData.getMobileNo())
                    .email(studentData.getEmail())
                    //.isActive(true)
                    .center(existingStudent.getCenter())  // Retain existing center
                    .build();


            if(studentData.getMarks()!= null && studentData.getMarks()>80){
                LevelMarks lm = LevelMarks.builder().level(student1.getLevel()-1).marks(studentData.getMarks())
                        .student(student1).
                        build();
                levelRepo.save(lm);
            }


        }else{

            student1 = Student.builder()
                    .regId(studentData.getRegId())
                    .birtDate(studentData.getBirtDate())
                    .center(centerRepo.findById(studentData.getCenterId()).get())
                    .standard(studentData.getStandard())
                    .level(studentData.getLevel())
                    .name(studentData.getName())
                    .address(studentData.getAddress())
                    .mobileNo(studentData.getMobileNo())
                    .email(studentData.getEmail())
                   // .isActive(true)
                    .build();





        }
        return studentRepo.save(student1);
    }





    @GetMapping("/teacher/{teacherId}")
    public List<AllStudentRes> getAllStudents(@PathVariable Long teacherId){
        List<AllStudentRes>studentDataList = new ArrayList<>();
        List<Centers>dummy = centerRepo.findByTeacher(teacherId);
        try{
            centerRepo.findByTeacher(teacherId).forEach(center->{

                List<Student>studentList= studentRepo.findByCenterId(center.getId());

                studentList.forEach(student ->{
                    AllStudentRes allStudentRes = new AllStudentRes();
                    allStudentRes.setLevel(student.getLevel());
                    allStudentRes.setId(student.getId());
                    allStudentRes.setReg_id(student.getRegId());
                    allStudentRes.setName(student.getName());
                    studentDataList.add(allStudentRes);


                });



            });

            return studentDataList;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @GetMapping("/getAll")
    public List<AllStudentRes> getAllStudents(){
        List<Student>studentList = studentRepo.findAll();
        List<AllStudentRes>studentDataList = new ArrayList<>();
        studentList.forEach(student ->{
            AllStudentRes allStudentRes = new AllStudentRes();
            allStudentRes.setName(student.getName());
            allStudentRes.setId(student.getId());
            allStudentRes.setReg_id(student.getRegId());
            allStudentRes.setLevel(student.getLevel());
            studentDataList.add(allStudentRes);
        });



        return studentDataList;

    }




    @GetMapping("/detail/{studentId}")
    public Student getStudentDetail(@PathVariable Long studentId){
        Student student = studentRepo.findById(studentId).get();

        return student;


    }



}
