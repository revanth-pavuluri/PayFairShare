package com.payfairshare.app.dto;

import lombok.Data;

@Data
public class UserRequestDto {
    private String name;
    private String username;
    private String password;
    private String upiId;
}
