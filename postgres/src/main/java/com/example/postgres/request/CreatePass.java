package com.example.postgres.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePass {
    private String email;
    private String username;
    private String password;

}
