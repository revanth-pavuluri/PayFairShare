package com.payfairshare.app.dto;

import java.util.List;


import lombok.Data;

@Data
public class GroupDto {
    private Long id;
    private String name;
    private List<ExpenseDto> expenses;
    private List<UserNameDto> groupUsers; 
}
