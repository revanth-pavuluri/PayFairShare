package com.payfairshare.app.dto;

import java.util.List;

import javax.persistence.Id;

import lombok.Data;

@Data
public class ExpenseDto {
	@Id
	 private Long id;
     private String name;
	 private Float amount;
	 private UserNameDto paidBy;
	 private Long groupId;
	 private String status;
	 private List<ExpenseSplitDto> expenseSplits;
	 private String createdOn;
}
