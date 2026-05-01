package com.example.postgres.Controller;

import com.example.postgres.Projection.AllStudentProjection;
import com.example.postgres.Projection.StudentProjectionWithMarks;
import com.example.postgres.entity.Centers;
import com.example.postgres.entity.LevelMarks;
import com.example.postgres.entity.Student;
import com.example.postgres.entity.Teacher;
import com.example.postgres.enums.Role;
import com.example.postgres.repository.*;
import com.example.postgres.request.StudentData;
import com.example.postgres.service.impl.StudentService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;


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

    @Autowired
    PasswordRepo passwordRepo;



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

            if(studentData.getLevel()==null){
                studentRepo.save(student1);
                return;
            }

            int level = studentData.getLevel();
            Integer marks = studentData.getMarks();
            LevelMarks lm = existingStudent.getLevelMarks();


            if (lm == null) {
                lm = new LevelMarks();
                lm.setStudent(existingStudent);
            }
            if (marks != null && marks > 80) {
                lm.setLevel(level+1);

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
                // set the level marks back to student
            }
            student1.setLevelMarks(lm);
            studentRepo.save(student1);
            levelRepo.save(lm);



        }else{


            student1 = Student.builder()
                    .regId(studentService.getNextRegId())
                    .birtDate(studentData.getBirtDate())
                    .center(centerRepo.findById(studentData.getCenterId()).get())
                    .standard(studentData.getStandard())
//                    .level(studentData.getLevel())
                    .name(studentData.getName())
                    .address(studentData.getAddress())
                    .mobileNo(studentData.getMobileNo())
                    .email(studentData.getEmail())
                    .type(studentData.getType())
                    .build();

//            if(studentData.getLevel()==null || studentData.getLevelMarks()==null){
//                studentRepo.save(student1);
//                return;
//            }

            int level = studentData.getLevel();
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
            student1.setLevelMarks(lm);
            studentRepo.save(student1);







        }
        return;
    }


    @GetMapping("/teacher/{teacherId}/students")
    public Page<AllStudentProjection> getStudentsByTeacher(
            @PathVariable Long teacherId,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("regId").descending());
        return studentRepo.findAllByTeacherAndSearch(teacherId, search.toLowerCase(), pageable);
    }



//    @GetMapping("/teacher/{teacherId}")
//    public List<AllStudentRes> getAllStudents(@PathVariable Long teacherId){
//        List<AllStudentRes>studentDataList = new ArrayList<>();
//        Teacher teacher = teacherRepo.findById(teacherId).get();
//        try{
//            centerRepo.findByTeacher(teacherId).forEach(center->{
//
//                List<Student>studentList= studentRepo.findByCenterId(center.getId());
//
//                studentList.forEach(student ->{
//                    AllStudentRes allStudentRes = new AllStudentRes();
//                    allStudentRes.setLevel(student.getLevel());
//                    allStudentRes.setId(student.getId());
//                    allStudentRes.setRegId(student.getRegId());
//                    allStudentRes.setName(student.getName());
//                    allStudentRes.setBirthDate(student.getBirtDate());
//                    allStudentRes.setTeacher(teacher.getName());
//                    studentDataList.add(allStudentRes);
//
//
//                });
//
//
//
//            });
//
//            return studentDataList;
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//
//    }


    @GetMapping("/getAll")
    public Page<AllStudentProjection> getAllStudents( @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "22000") int size){
        Pageable pageable = PageRequest.of(page,size);
        return studentRepo.findAllProjected(pageable);

    }

    @GetMapping("/getAlls")
    public Page<AllStudentProjection> getAllStudent( @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "22000") int size,@RequestParam String search){
        Pageable pageable = PageRequest.of(page,size);
        return studentRepo.findAllProjectedWithSearch(search,pageable);

    }







    @GetMapping("/totalCount")
    public long getTotalCount(@RequestParam Long teacherId,@RequestParam String searchText){
       Teacher teacher = teacherRepo.findById(teacherId).get();
        if (teacher.getRoles() == Role.TEACHER) {
            return studentRepo.findTotalStudentsForTeacher(teacherId,searchText);
        } else {
            return studentRepo.findTotalStudentsForAdmin(searchText);
        }


    }




    @GetMapping("/detail/{studentId}")
    public Student getStudentDetail(@PathVariable Long studentId){
        Student student = studentRepo.findById(studentId).get();


        return student;


    }

    @GetMapping("/downloadForTeacher")
    public Page<StudentProjectionWithMarks> downloadForTeacher(@RequestParam Long teacherId, @RequestParam(required = false,defaultValue = "") String searchText){
        Pageable pageable = PageRequest.of(0, 5000, Sort.by("regId").descending());

            return studentRepo.findForDownloadByTeacher(teacherId,searchText,pageable);


    }


    @GetMapping("/downloadAll")
    public Page<StudentProjectionWithMarks> downloadAllStudents(@RequestParam(required = false) String search) {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("regId").descending());
        return studentRepo.findForAdminDownload(search,pageable);


    }


    @PostMapping("/changeCenter")
    public ResponseEntity<String> changeStudentCenter(
            @RequestParam Long studentId,
            @RequestParam Long newCenterId
    ) {
        try {
            // 1. Find the student
            Student student = studentRepo.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

            // 2. Find the new center
            Centers newCenter = centerRepo.findById(newCenterId)
                    .orElseThrow(() -> new RuntimeException("Center not found with id: " + newCenterId));

            // 3. Get old center info for response
            String oldCenterName = student.getCenter() != null ? student.getCenter().getName() : "No Center";

            // 4. Change the center
            student.setCenter(newCenter);

            // 5. Save the student
            studentRepo.save(student);

            // 6. Return success message
            return ResponseEntity.ok(
                    String.format("Student '%s' (Reg: %d) moved from '%s' to '%s'",
                            student.getName(),
                            student.getRegId(),
                            oldCenterName,
                            newCenter.getName()
                    )
            );

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to change center: " + e.getMessage());
        }
    }


    /**
     * Get all centers in the system (for admin to choose from)
     *
     * @return List of all centers with their teacher info
     */
    @GetMapping("/getAllCentersForAdmin")
    public ResponseEntity<List<CenterInfo>> getAllCentersForAdmin() {
        try {
            List<Centers> allCenters = centerRepo.findAll();

            List<CenterInfo> centerInfoList = allCenters.stream()
                    .map(center -> {
                        CenterInfo info = new CenterInfo();
                        info.setCenterId(center.getId());
                        info.setCenterName(center.getName());
                        info.setTeacherId(center.getTeacher().getId());
                        info.setTeacherName(center.getTeacher().getName());
                        return info;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(centerInfoList);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // DTO class for center info (add this as inner class or separate file)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CenterInfo {
        private Long centerId;
        private String centerName;
        private Long teacherId;
        private String teacherName;
    }


}
