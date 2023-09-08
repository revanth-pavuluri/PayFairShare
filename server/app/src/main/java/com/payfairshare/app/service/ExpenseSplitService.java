package com.payfairshare.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.payfairshare.app.dto.ExpenseSplitDto;
import com.payfairshare.app.mapper.ExpenseMapper;
import com.payfairshare.app.repository.ExpenseRepository;
import com.payfairshare.app.repository.ExpenseSplitRepsitory;


@Service
public class ExpenseSplitService {
    @Autowired
    private ExpenseSplitRepsitory expenseSplitRepsitory;
    @Autowired
    private ExpenseMapper expenseMapper;
    @Autowired
    private ExpenseRepository expenseRepository;

    public ResponseEntity<List<ExpenseSplitDto>> getExpenseSplit(Long expId){
        try{
            if(expenseRepository.findById(expId).orElse(null) == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such expense found");
            }
           return ResponseEntity.ok(expenseMapper.toExpenseSplitDtos(expenseSplitRepsitory.findByExpenseId(expId)));
        }catch(ResponseStatusException e){
            throw new ResponseStatusException(e.getStatus(),e.getReason());
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }
}
