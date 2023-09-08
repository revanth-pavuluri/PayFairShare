package com.payfairshare.app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.payfairshare.app.dto.ExpenseDto;
import com.payfairshare.app.dto.ExpenseSplitDto;
import com.payfairshare.app.mapper.ExpenseMapper;
import com.payfairshare.app.models.Expense;
import com.payfairshare.app.models.ExpenseSplit;
import com.payfairshare.app.models.Group;
import com.payfairshare.app.models.User;
import com.payfairshare.app.repository.ExpenseRepository;
import com.payfairshare.app.repository.GroupRepository;
import com.payfairshare.app.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private ExpenseMapper expenseMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;

    
    public ResponseEntity<ExpenseDto> createExpense(ExpenseDto expenseDto) {
        expenseDto.setStatus("Pending");
        Expense expense = expenseRepository.save(expenseMapper.toExpense(expenseDto));

        List<ExpenseSplit> expenseSplits = new ArrayList<>();
        for (ExpenseSplitDto splitDTO : expenseDto.getExpenseSplits())
         {
            ExpenseSplit expenseSplit = expenseMapper.toExpenseSplit(splitDTO, expense);
            expenseSplit.setStatus("Pending");
            expenseSplits.add(expenseSplit);
        }
        expense.setExpenseSplits(expenseSplits);
        try{
            return ResponseEntity.ok(expenseMapper.toExpenseDto(expenseRepository.save(expense)));
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

    
    public ResponseEntity<List<ExpenseDto>> getGrpExpenses(Long groupId) {
        try{
            if(groupRepository.findById(groupId).orElse(null) == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Group not found");
            }
            if(expenseRepository.findByGroupId(groupId).size() == 0){
                throw new ResponseStatusException(HttpStatus.NO_CONTENT,"No expenses in this group yet.");
            }
            return ResponseEntity.ok(expenseMapper.toExpenseDtos(expenseRepository.findByGroupId(groupId)));
        }catch(ResponseStatusException e){
            throw new ResponseStatusException(e.getStatus(),e.getReason());
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    public ResponseEntity<List<ExpenseDto>> getUserExpenses(Long userId) {
        try{
            User user = userRepository.findById(userId).get();
            if(user != null){
                List<Expense> list = user.getExpenses();
                if(list.size() != 0){
                    return ResponseEntity.ok(expenseMapper.toExpenseDtos(list));
                }
                else{
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No expenses found");
                }
            }else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found");
            }
        }catch(ResponseStatusException e){
            throw new ResponseStatusException(e.getStatus(),e.getReason());
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        
    }
       public ResponseEntity<List<ExpenseDto>> getGroupExpenses(Long groupId) {
        try{
            Group group = groupRepository.findById(groupId).get();
            if(group != null){
                List<Expense> list = group.getExpenses();
                if(list.size() != 0){
                    return ResponseEntity.ok(expenseMapper.toSimpleExpenseDtos(list));
                }
                else{
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No expenses found");
                }
            }else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Group not found");
            }
        }catch(ResponseStatusException e){
            throw new ResponseStatusException(e.getStatus(),e.getReason());
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    
    public ResponseEntity<String> deleteExpense(Long expId, Long userId) {
        try{
            Expense expense = expenseRepository.findByIdAndUserId(expId, userId).orElse(null);
            if(expense == null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Unauthorised request!!");
            }
            expenseRepository.deleteById(expId);
            return ResponseEntity.ok("Expense deleted successfully!");
        }catch(ResponseStatusException e){
            throw new ResponseStatusException(e.getStatus(),e.getReason());
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public void updateExpenseStatusForGroup(Long groupId) {
        List<Expense> pendingExpenses = expenseRepository.findByGroupIdAndStatus(groupId, "Pending");
        
        for (Expense expense : pendingExpenses) {
            expense.setStatus("Finalized");
            for (ExpenseSplit split : expense.getExpenseSplits()) {
                split.setStatus("Finalized");
            }
        }
        
        expenseRepository.saveAll(pendingExpenses);
    }
}
