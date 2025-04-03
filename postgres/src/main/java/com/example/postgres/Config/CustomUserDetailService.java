package com.example.postgres.Config;
import com.example.postgres.entity.Teacher;
import com.example.postgres.repository.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private TeacherRepo teacherRepo;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Teacher teacher = teacherRepo.findByUsername(username);



        if(teacher == null){
            throw new UsernameNotFoundException("user not found");

        }

        return User.withUsername(teacher.getUsername()).password(teacher.getPassword()).roles(teacher.getRoles().name()).build();
    }
}
