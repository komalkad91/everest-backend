package com.example.postgres.Controller;

import com.example.postgres.Projection.AllStudentProjection;
import com.example.postgres.entity.Centers;
import com.example.postgres.entity.LevelMarks;
import com.example.postgres.entity.Student;
import com.example.postgres.entity.Teacher;
import com.example.postgres.repository.CenterRepo;
import com.example.postgres.repository.LevelRepo;
import com.example.postgres.repository.StudentRepo;
import com.example.postgres.repository.TeacherRepo;
import com.example.postgres.request.AllStudentRes;
import com.example.postgres.request.StudentData;
import com.example.postgres.service.impl.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    TeacherRepo teacherRepo;

    @Autowired
    StudentService studentService;



    @PostMapping("/add/{teacherId}")
    public void addStudent(@PathVariable Long teacherId, @RequestBody StudentData studentData){
        Student student1;
        if(studentData.getId() != null){


            Student existingStudent = studentRepo.findById(studentData.getId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));


            student1 = Student.builder()
                    .id(existingStudent.getId())
                    .regId(studentData.getRegId())
                    .birtDate(studentData.getBirtDate())
                    .standard(studentData.getStandard())
                    .level(studentData.getLevel()+1)
                    .name(studentData.getName())
                    .address(studentData.getAddress())
                    .mobileNo(studentData.getMobileNo())
                    .email(studentData.getEmail())
                    .center(existingStudent.getCenter())
                    .type(studentData.getType())
                    .build();

            if (studentData.getAssignTeacher()!=null){
                Teacher teacher = teacherRepo.findById(studentData.getAssignTeacher()).get();
                student1.setCenter(teacher.getCentersList().get(0));
            }

            int level = studentData.getLevel();
            Integer marks = studentData.getMarks();
            LevelMarks lm = existingStudent.getLevelMarks();
            if (lm == null) {
                lm = new LevelMarks();
                lm.setStudent(existingStudent);
            }
            if (marks != null && marks > 80) {
                switch (level) {
                    case 0: lm.setL_f(marks); break;
                    case 1: lm.setL_1(marks); break;
                    case 2: lm.setL_2(marks); break;
                    case 3: lm.setL_3(marks); break;
                    case 4: lm.setL_4(marks); break;
                    case 5: lm.setL_5(marks); break;
                    case 6: lm.setL_6(marks); break;
                    case 7: lm.setL_7(marks); break;
                    case 8: lm.setL_8(marks); break;
                    default: break;
                }
                student1.setLevelMarks(lm); // set the level marks back to student
            }
                studentRepo.save(student1);


//            levelRepo.save(lm);



        }else{


            student1 = Student.builder()
                    .regId(studentService.getNextRegId())
                    .birtDate(studentData.getBirtDate())
                    .center(centerRepo.findById(studentData.getCenterId()).get())
                    .standard(studentData.getStandard())
                    .level(studentData.getLevel())
                    .name(studentData.getName())
                    .address(studentData.getAddress())
                    .mobileNo(studentData.getMobileNo())
                    .email(studentData.getEmail())
                    .type(studentData.getType())
                    .build();

            int level = student1.getLevel();
            Integer marks = studentData.getMarks();
            LevelMarks lm = new LevelMarks();
            lm.setStudent(student1);
            if (marks != null && marks > 80) {
                switch (level) {
                    case 1: lm.setL_f(marks); break;
                    case 2: lm.setL_1(marks); break;
                    case 3: lm.setL_2(marks); break;
                    case 4: lm.setL_3(marks); break;
                    case 5: lm.setL_4(marks); break;
                    case 6: lm.setL_5(marks); break;
                    case 7: lm.setL_6(marks); break;
                    case 8: lm.setL_7(marks); break;
                    case 9: lm.setL_8(marks); break;
                    default: break;
                }
                student1.setLevelMarks(lm); // set the level marks back to student
            }

            studentRepo.save(student1);







        }
        return;
    }





    @GetMapping("/teacher/{teacherId}")
    public List<AllStudentRes> getAllStudents(@PathVariable Long teacherId){
        List<AllStudentRes>studentDataList = new ArrayList<>();
        Teacher teacher = teacherRepo.findById(teacherId).get();
        try{
            centerRepo.findByTeacher(teacherId).forEach(center->{

                List<Student>studentList= studentRepo.findByCenterId(center.getId());

                studentList.forEach(student ->{
                    AllStudentRes allStudentRes = new AllStudentRes();
                    allStudentRes.setLevel(student.getLevel());
                    allStudentRes.setId(student.getId());
                    allStudentRes.setRegId(student.getRegId());
                    allStudentRes.setName(student.getName());
                    allStudentRes.setBirthDate(student.getBirtDate());
                    allStudentRes.setTeacher(teacher.getName());
                    studentDataList.add(allStudentRes);


                });



            });

            return studentDataList;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


    @GetMapping("/getAll")
    public Page<AllStudentProjection> getAllStudents( @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "22000") int size){
        Pageable pageable = PageRequest.of(page,size);
        return studentRepo.findAllProjected(pageable);


    }

    @GetMapping("/totalCount")
    public long getTotalCount(@RequestParam Long teacherId){
       Teacher teacher = teacherRepo.findById(teacherId).get();
       if(teacher.getRoles().equals("TEACHER")){
        return   studentRepo.findTotalStudents(teacherId);


       }else{
          return studentRepo.count();
       }


    }




    @GetMapping("/detail/{studentId}")
    public Student getStudentDetail(@PathVariable Long studentId){
        Student student = studentRepo.findById(studentId).get();

        return student;


    }


}
