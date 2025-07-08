package com.example.postgres.service.impl;

import com.example.postgres.request.CreatePass;
import com.example.postgres.request.login;
import org.springframework.http.ResponseEntity;

public interface loginImpl {

     void savePassword();

     ResponseEntity<String> logInUSer(login log);
}
