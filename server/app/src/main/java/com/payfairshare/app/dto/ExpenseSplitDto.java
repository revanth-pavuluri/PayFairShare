package com.payfairshare.app.dto;

import javax.persistence.Id;

import lombok.Data;

@Data
public class ExpenseSplitDto {
    @Id
    private Long id;
    private Long expenseId;
    private Long userId;
	private Float userResponsibleAmount;
    private String status;
}
