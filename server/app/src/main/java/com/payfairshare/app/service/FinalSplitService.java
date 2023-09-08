package com.payfairshare.app.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.payfairshare.app.dto.FinalSplitDto;
import com.payfairshare.app.mapper.FinalSplitMapper;
import com.payfairshare.app.mapper.UserMapper;
import com.payfairshare.app.models.Expense;
import com.payfairshare.app.models.ExpenseSplit;
import com.payfairshare.app.models.FinalSplit;
import com.payfairshare.app.models.Group;
import com.payfairshare.app.models.User;
import com.payfairshare.app.repository.ExpenseRepository;
import com.payfairshare.app.repository.ExpenseSplitRepsitory;
import com.payfairshare.app.repository.FinalSplitRepository;
import com.payfairshare.app.repository.GroupRepository;



@Service
public class FinalSplitService{

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private ExpenseSplitRepsitory expenseSplitRepository;
    @Autowired
    private FinalSplitMapper finalSplitMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ExpenseService expenseService;


    @Autowired
    private FinalSplitRepository finalSplitRepository;

    public ResponseEntity<String> updateStatus(FinalSplitDto finalSplitDto, Long userId){
        try{
            if(finalSplitDto.getPayTo().getId() != userId && finalSplitDto.getPayBy().getId() != userId){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Unauthorised request!!");
            }
            FinalSplit finalsplit = finalSplitRepository.findById(finalSplitDto.getId()).orElse(null);
            finalsplit.setStatus("Paid");
            finalSplitRepository.save(finalsplit);
            return ResponseEntity.ok("Status updated");
        }catch(ResponseStatusException e){
            throw new ResponseStatusException(e.getStatus(),e.getReason());
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

      public ResponseEntity<List<FinalSplitDto>> getGroupFinal(Long groupId) {
        try{
            Group group = groupRepository.findById(groupId).get();
            if(group != null){
                List<FinalSplit> list = group.getFinalSplits();
                List<FinalSplitDto> dtoList = finalSplitMapper.toListFinalsplitDto(list);
                for(FinalSplitDto dto : dtoList){
                    dto.setPayTo(userMapper.toUserNameDto(groupRepository.findById(groupId).orElse(null)
                                            .getGroupUsers().stream()
                                            .filter(user -> user.getId().equals(dto.getPayTo().getId()))
                                            .findFirst()
                                            .orElse(null)));
                    dto.setPayBy(userMapper.toUserNameDto(groupRepository.findById(groupId).orElse(null)
                                            .getGroupUsers().stream()
                                            .filter(user -> user.getId().equals(dto.getPayBy().getId()))
                                            .findFirst()
                                            .orElse(null)));
                }
                if(list.size() != 0){
                    return ResponseEntity.ok(dtoList);
                }
                else{
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No final splits found");
                }
            }else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id not found");
            }
        }catch(ResponseStatusException e){
            throw new ResponseStatusException(e.getStatus(),e.getReason());
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

	public ResponseEntity<List<FinalSplitDto>> getFinalSplit(Long groupId) {
        try{
		// Retrieve all expenses of the group
	    List<Expense> groupExpenses = expenseRepository.findByGroupIdAndStatus(groupId,"Pending");

        if(groupExpenses.size() == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No Pending splits found");
        }
	    // Create a map to store individual user contributions
	    Map<Long, Float> userContributions = new HashMap<>();
	    
	 // Calculate individual user contributions for each expense
	    for (Expense expense : groupExpenses) {
	        Long expenseId = expense.getId();
	        
	        // Retrieve all ExpenseSplit records for the current expense
	        List<ExpenseSplit> expenseSplits = expenseSplitRepository.findByExpenseId(expenseId);
	        
	        // Calculate individual user contributions for the expense
	        for (ExpenseSplit expenseSplit : expenseSplits) {
	            Long userId = expenseSplit.getUser().getId();
	            Float userContribution;
	            if(userId == expense.getPaidBy().getId()) {
	            	userContribution = Float.valueOf(expenseSplit.getUserResponsibleAmount()) - expense.getAmount();
	            }
	            else {
	            	userContribution = Float.valueOf(expenseSplit.getUserResponsibleAmount());
	            }
	            // Update user contributions
	            userContributions.put(userId, userContributions.getOrDefault(userId,0.0f) + (userContribution));
	        }
	    }
	    Map<Long, Map<Long, Float>> result = findMinimumPairs(userContributions);
        Group group = groupRepository.findById(groupId).get();
        List<FinalSplitDto> finalSplitDtos = new ArrayList<>();
        for (Map.Entry<Long, Map<Long, Float>> entry : result.entrySet()) {
            Long payerId = entry.getKey();
            Map<Long, Float> payments = entry.getValue();
            List<User> groupUsers = group.getGroupUsers();
            List<FinalSplit> finalSplits = new ArrayList<>();
            FinalSplitDto finalSplitDto = new FinalSplitDto();
            FinalSplit finalSplit = new FinalSplit();
            for (Map.Entry<Long, Float> payment : payments.entrySet()) {
                Long receiverId = payment.getKey();
                Float amount = payment.getValue();
                finalSplitDto.setAmount((amount));
                finalSplitDto.setPayBy(userMapper.toUserNameDto(groupUsers.stream()
                                            .filter(user -> user.getId().equals(payerId))
                                            .findFirst()
                                            .orElse(null)));
                finalSplitDto.setPayTo(userMapper.toUserNameDto(groupUsers.stream()
                                            .filter(user -> user.getId().equals(receiverId))
                                            .findFirst()
                                            .orElse(null)));
                finalSplitDto.setStatus("Pending");
                finalSplitDtos.add(finalSplitDto);
                finalSplit = finalSplitMapper.finalSplitDtoToFinalSplit(finalSplitDto);
                finalSplit.setGroupId(groupId);
                finalSplits.add(finalSplit);
                expenseService.updateExpenseStatusForGroup(groupId);
                System.out.println("User " + payerId + " pays " + amount + " to User " + receiverId);
            }
        
            finalSplitRepository.saveAll(finalSplits);
        }
		return ResponseEntity.ok(finalSplitDtos);
        }catch(ResponseStatusException e){
            throw new ResponseStatusException(e.getStatus(),e.getReason());
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
	}
	public static Map<Long, Map<Long, Float>> findMinimumPairs(Map<Long, Float> idToAmount) {
        // Separate positive and negative amounts with user IDs
        Map<Long, Float> positiveAmounts = new HashMap<>();
        Map<Long, Float> negativeAmounts = new HashMap<>();
        
        for (Map.Entry<Long, Float> entry : idToAmount.entrySet()) {
            Long userId = entry.getKey();
            Float amount = entry.getValue();
            
            if (amount > 0) {
                positiveAmounts.put(userId, amount);
            } else if (amount < 0) {
                negativeAmounts.put(userId, Math.abs(amount));
            }
        }
        
        // Initialize the result map
        Map<Long, Map<Long, Float>> result = new HashMap<>();
        
        // Iterate through positive amounts to find pairs
        for (Map.Entry<Long, Float> positiveEntry : positiveAmounts.entrySet()) {
            Long payerId = positiveEntry.getKey();
            Float payerAmount = positiveEntry.getValue();
            
            // Iterate through negative amounts to find receivers
            for (Map.Entry<Long, Float> negativeEntry : negativeAmounts.entrySet()) {
                Long receiverId = negativeEntry.getKey();
                Float receiverAmount = negativeEntry.getValue();
                
                // Calculate the amount to be paid from payer to receiver
                Float paymentAmount = Math.min(payerAmount, receiverAmount);
                
                // Update payer's amount
                payerAmount -= paymentAmount;
                
                // Update receiver's amount
                receiverAmount -= paymentAmount;
                
                // Create a payment entry in the result map
                result
                    .computeIfAbsent(payerId, k -> new HashMap<>())
                    .put(receiverId, paymentAmount);
                
                // Remove entries with zero or negative amounts
                if (receiverAmount <= 0) {
                    negativeAmounts.remove(receiverId);
                }
                
                if (payerAmount <= 0) {
                    break;
                }
            }
        }
        
        return result;
    }

}

