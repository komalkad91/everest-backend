package com.example.postgres.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class request {
     private Long teacher_id;
     private String subject;
     private String requestType;
     private String message;
     private String requestStatus;
}
