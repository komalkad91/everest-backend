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
import com.example.postgres.service.CertificateService;
import com.example.postgres.service.impl.StudentService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Autowired
    CertificateService certificateService;



    @PostMapping("/add/{teacherId}")
    public ResponseEntity<String> addStudent(@PathVariable Long teacherId, @RequestBody StudentData studentData){
        // VALIDATION: Type is REQUIRED
        if (studentData.getType() == null || studentData.getType().equals("")) {
            return ResponseEntity.badRequest()
                    .body("Student Type is required (F/K/P/S)");
        }
        
        Student student1;
        if(studentData.getId() != null){


            Student existingStudent = studentRepo.findById(studentData.getId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));


            // FIX: Handle NULL level safely
            Integer nextLevel = null;
            if (studentData.getLevel() != null && studentData.getMarks()!=null && studentData.getMarks()>80) {
                nextLevel = studentData.getLevel() + 1;
            }else if(studentData.getLevel()!=null){
                nextLevel = studentData.getLevel();
            }
            
            student1 = Student.builder()
                    .id(existingStudent.getId())
                    .regId(studentData.getRegId())
                    .birtDate(studentData.getBirtDate())
                    .standard(studentData.getStandard())
                    .level(nextLevel)  // NULL-safe now!
                    .name(studentData.getName())
                    .address(studentData.getAddress())
                    .mobileNo(studentData.getMobileNo())
                    .email(studentData.getEmail())
                    .center(existingStudent.getCenter())
                    .type(studentData.getType())
                    .certificate(existingStudent.getCertificate())  // PRESERVE CERTIFICATE!
                    .build();

            if (studentData.getAssignTeacher()!=null){
                Teacher teacher = teacherRepo.findById(studentData.getAssignTeacher()).get();
                student1.setCenter(teacher.getCentersList().get(0));
            }

            // Save student basic info even if no marks added
            if(studentData.getLevel()==null){
                studentRepo.save(student1);
                return ResponseEntity.ok("Student updated successfully");
            }

            int level = studentData.getLevel();
            Integer marks = studentData.getMarks();
            LevelMarks lm = existingStudent.getLevelMarks();

           //this condition is additional
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
                    case 9: lm.setL_9(marks); break;
                    case 10: lm.setL_10(marks); break;
                    default: break;
                }
                // Update student's level to match LevelMarks level
                student1.setLevel(level + 1);
            }
            student1.setLevelMarks(lm);
            studentRepo.save(student1);
            levelRepo.save(lm);
            return ResponseEntity.ok("Student updated with marks successfully");

        }else{


            // FIX: Set initial level based on type
            // Type F (Foundation) starts at level 0, others start at level 1
            Integer initialLevel = "Foundation".equals(studentData.getType()) ? 0 : 1;
            
            student1 = Student.builder()
                    .regId(studentService.getNextRegId())
                    .birtDate(studentData.getBirtDate())
                    .center(centerRepo.findById(studentData.getCenterId()).get())
                    .standard(studentData.getStandard())
                    .level(initialLevel)  // Set based on type!
                    .name(studentData.getName())
                    .address(studentData.getAddress())
                    .mobileNo(studentData.getMobileNo())
                    .email(studentData.getEmail())
                    .type(studentData.getType())
                    .build();

            // Create empty LevelMarks entry (will be filled when marks are added)
            LevelMarks lm = new LevelMarks();
            lm.setStudent(student1);
            
            // Set initial level in LevelMarks too
            lm.setLevel(initialLevel);
            
            // If marks provided during student creation, save them
            if (studentData.getLevel() != null && studentData.getMarks() != null) {
                int level = studentData.getLevel();
                Integer marks = studentData.getMarks();
                
                if (marks > 80) {
                    lm.setLevel(level + 1);  // Update to next level
                    
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
                        case 10: lm.setL_9(marks); break;
                        case 11: lm.setL_10(marks); break;
                        default: break;
                    }
                }
            }
            
            student1.setLevelMarks(lm);
            studentRepo.save(student1);
            levelRepo.save(lm);  // ALWAYS save LevelMarks
            return ResponseEntity.ok("Student created successfully");
        }
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


    /**
     * Update student certificate level
     * Only allows distributing certificates for completed levels (marks > 80)
     */
    @PostMapping("/updateCertificate")
    public ResponseEntity<String> updateCertificate(
            @RequestParam Long studentId,
            @RequestParam Integer certificateLevel
    ) {
        try {
            Student student = studentRepo.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            LevelMarks marks = student.getLevelMarks();
            if (marks == null) {
                return ResponseEntity.badRequest()
                        .body("Student has no level marks");
            }

            if (certificateLevel < 0 || certificateLevel > 10) {
                return ResponseEntity.badRequest()
                        .body("Invalid certificate level. Must be between 0 and 10");
            }

            // Check if level is completed
            boolean levelCompleted = false;
            Integer levelMarks = null;

            switch(certificateLevel) {
                case 0: levelMarks = marks.getL_f(); break;
                case 1: levelMarks = marks.getL_1(); break;
                case 2: levelMarks = marks.getL_2(); break;
                case 3: levelMarks = marks.getL_3(); break;
                case 4: levelMarks = marks.getL_4(); break;
                case 5: levelMarks = marks.getL_5(); break;
                case 6: levelMarks = marks.getL_6(); break;
                case 7: levelMarks = marks.getL_7(); break;
                case 8: levelMarks = marks.getL_8(); break;
                case 9: levelMarks = marks.getL_9(); break;
                case 10: levelMarks = marks.getL_10(); break;
            }

            levelCompleted = levelMarks != null && levelMarks > 80;

            if (!levelCompleted) {
                if (levelMarks == null) {
                    return ResponseEntity.badRequest()
                            .body("Cannot distribute certificate. Level not attempted");
                } else {
                    return ResponseEntity.badRequest()
                            .body("Cannot distribute certificate. Marks: " + levelMarks + " (needs >80)");
                }
            }

            // Smarter validation: Allow distributing certificates in order
            // But allow skipping levels the student never attempted
            Integer currentCert = student.getCertificate() != null ? student.getCertificate() : -1;
            
            // Find all levels with passing marks below the requested certificate level
            boolean canDistribute = true;
            for (int i = currentCert + 1; i < certificateLevel; i++) {
                Integer checkMarks = null;
                switch(i) {
                    case 0: checkMarks = marks.getL_f(); break;
                    case 1: checkMarks = marks.getL_1(); break;
                    case 2: checkMarks = marks.getL_2(); break;
                    case 3: checkMarks = marks.getL_3(); break;
                    case 4: checkMarks = marks.getL_4(); break;
                    case 5: checkMarks = marks.getL_5(); break;
                    case 6: checkMarks = marks.getL_6(); break;
                    case 7: checkMarks = marks.getL_7(); break;
                    case 8: checkMarks = marks.getL_8(); break;
                    case 9: checkMarks = marks.getL_9(); break;
                    case 10: checkMarks = marks.getL_10(); break;
                }
                // If there's a level with passing marks that doesn't have certificate yet, can't skip it
                if (checkMarks != null && checkMarks > 80) {
                    canDistribute = false;
                    return ResponseEntity.badRequest()
                            .body("Cannot skip Level " + i + " certificate. Student passed it with marks: " + checkMarks);
                }
            }

            // Update certificate
            student.setCertificate(certificateLevel);
            studentRepo.save(student);

            String levelName = certificateLevel == 0 ? "Foundation" : "Level " + certificateLevel;
            return ResponseEntity.ok(
                    String.format("Certificate for %s distributed to %s (Reg: %d)",
                            levelName, student.getName(), student.getRegId())
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }


    /**
     * Get students with pending certificates
     */
    @GetMapping("/studentsNeedingCertificates")
    public ResponseEntity<List<PendingCertInfo>> getStudentsNeedingCertificates() {
        try {
            List<Student> allStudents = studentRepo.findAll();

            List<PendingCertInfo> pendingList = allStudents.stream()
                    .filter(student -> {
                        LevelMarks lm = student.getLevelMarks();
                        if (lm == null || lm.getLevel() == null) return false;

                        int completedLevel = lm.getLevel() - 1;
                        Integer cert = student.getCertificate() != null ? student.getCertificate() : -1;

                        return completedLevel > cert;
                    })
                    .map(student -> {
                        PendingCertInfo info = new PendingCertInfo();
                        info.setStudentId(student.getId());
                        info.setStudentName(student.getName());
                        info.setRegId(student.getRegId());
                        info.setPendingCount((student.getLevelMarks().getLevel() - 1) -
                                (student.getCertificate() != null ? student.getCertificate() : -1));
                        return info;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(pendingList);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Download certificate PDF for a student
     * GET /api/student/certificate/{studentId}
     */
    @GetMapping("/certificate/{studentId}")
    public ResponseEntity<byte[]> downloadCertificate(@PathVariable Long studentId) {
        try {
            System.out.println("Certificate request for student ID: " + studentId);
            
            Student student = studentRepo.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            
            System.out.println("Found student: " + student.getName());

            Integer level = student.getLevel();
            System.out.println("Student level: " + level);
            
            String levelText = certificateService.getLevelText(level);
            System.out.println("Level text: " + levelText);

            Integer marks = 0;
            if (student.getLevelMarks() != null) {
                marks = getMarksForLevel(student.getLevelMarks(), level - 1);
            }
            System.out.println("Marks: " + marks);

            // Get teacher name from student's center
            String teacherName = "";
            if (student.getCenter() != null && student.getCenter().getTeacher() != null) {
                teacherName = student.getCenter().getTeacher().getName();
            }
            System.out.println("Teacher name: " + teacherName);

            byte[] pdfBytes = certificateService.generateCertificate(
                    student.getName(),
                    levelText,
                    marks,
                    teacherName
            );
            
            System.out.println("PDF generated successfully, size: " + pdfBytes.length);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData(
                    "attachment",
                    "Certificate_" + student.getName().replace(" ", "_") + ".pdf"
            );

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            System.err.println("Error generating certificate: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Helper method to get marks for a specific level
     */
    private Integer getMarksForLevel(LevelMarks levelMarks, Integer level) {
        switch (level) {
            case 0: return levelMarks.getL_f();
            case 1: return levelMarks.getL_1();
            case 2: return levelMarks.getL_2();
            case 3: return levelMarks.getL_3();
            case 4: return levelMarks.getL_4();
            case 5: return levelMarks.getL_5();
            case 6: return levelMarks.getL_6();
            case 7: return levelMarks.getL_7();
            case 8: return levelMarks.getL_8();
            case 9: return levelMarks.getL_9();
            case 10: return levelMarks.getL_10();
            default: return 0;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PendingCertInfo {
        private Long studentId;
        private String studentName;
        private Long regId;
        private Integer pendingCount;
    }


}
