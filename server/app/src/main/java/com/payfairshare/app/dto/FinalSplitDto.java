package com.payfairshare.app.dto;


import lombok.Data;

@Data
public class FinalSplitDto {
    private Long id;
    private Long groupId;
    private UserNameDto payTo;
    private UserNameDto payBy;
    private Float amount;
    private String status;
    // private String createdOn;
}
