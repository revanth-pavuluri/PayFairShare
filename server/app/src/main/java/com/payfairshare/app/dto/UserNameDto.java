package com.payfairshare.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserNameDto {
    private Long id;
    private String name;

    public UserNameDto(Long id){
        this.id = id;
    }
}
