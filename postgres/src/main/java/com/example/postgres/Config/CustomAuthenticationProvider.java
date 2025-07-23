package com.example.postgres.Config;

import com.example.postgres.entity.Teacher;
import com.example.postgres.repository.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private TeacherRepo teacherRepo;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        Teacher teacher = teacherRepo.findByUsername(username);

        return new UsernamePasswordAuthenticationToken(
                teacher,
                password,
                Collections.singletonList(new SimpleGrantedAuthority("USER")) // Roles
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
