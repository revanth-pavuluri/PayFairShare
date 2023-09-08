package com.payfairshare.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.payfairshare.app.dto.ExpenseDto;
import com.payfairshare.app.service.ExpenseService;

import java.util.List;

@RestController
@RequestMapping("/expense")
@CrossOrigin(origins = "*")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/create/{groupId}")
    public ResponseEntity<ExpenseDto> createExpense(@RequestBody ExpenseDto expenseModel,@PathVariable Long groupId){
        return expenseService.createExpense(expenseModel);
    }
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<ExpenseDto>> getGrpExpenses(@PathVariable Long groupId){
        return expenseService.getGrpExpenses(groupId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExpenseDto>> getUserExpenses(@PathVariable Long userId){
        return expenseService.getUserExpenses(userId);
    }

    @DeleteMapping("/delete/{expId}/{userId}")
    public ResponseEntity<String> resolveExpense(@PathVariable Long expId,@PathVariable Long userId){
        return expenseService.deleteExpense(expId,userId);
    }
}
