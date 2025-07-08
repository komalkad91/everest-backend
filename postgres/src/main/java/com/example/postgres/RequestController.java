package com.example.postgres;

import com.example.postgres.entity.Request;
import com.example.postgres.entity.Teacher;
import com.example.postgres.repository.RequestRepo;
import com.example.postgres.repository.TeacherRepo;
import com.example.postgres.request.request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://everest-abacus.cloudjiffy.net/"})
public class RequestController {
     
    @Autowired
    RequestRepo repo;

    @Autowired
    TeacherRepo repo1;

    @PostMapping("/addRequest")
    public void addRequest(@RequestBody request request1){
        Request req = new Request();
        Teacher teacher = repo1.getReferenceById(request1.getTeacher_id());
        req.setTeacher(teacher);
        req.setRequestType(request1.getRequestType());
        req.setRequestStatus(request1.getRequestStatus());
        req.setMessage(request1.getMessage());
        req.setSubject(request1.getSubject());
        repo.save(req);


    }


    @GetMapping("/getRequest")
    public List<Request> getRequest(@RequestParam("teacherId") Long teacherId)  {


        return repo.findByTeacherId(teacherId);
    }

}
