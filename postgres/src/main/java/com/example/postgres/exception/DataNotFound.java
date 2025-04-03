package com.example.postgres.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class DataNotFound extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataNotFound(String msg) {
        super(msg);
        this.msg =msg;

    }


}
