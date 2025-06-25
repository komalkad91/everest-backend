package com.example.postgres.service;

import com.example.postgres.service.impl.StudentService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class StudentServiceImpl implements StudentService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long getNextRegId()
    {
        Number nextVal = (Number) entityManager
                .createNativeQuery("SELECT nextval('reg_id_seq')")
                .getSingleResult();
        return nextVal.longValue();
    }


}
