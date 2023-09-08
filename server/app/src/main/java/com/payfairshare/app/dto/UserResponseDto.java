package com.payfairshare.app.dto;

import java.util.List;


import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private String name;
    private List<GroupDto> userGroups;
    private List<ExpenseDto> expenses;
}
