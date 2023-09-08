package com.payfairshare.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.payfairshare.app.dto.ExpenseDto;
import com.payfairshare.app.dto.GroupDto;
import com.payfairshare.app.mapper.ExpenseMapper;
import com.payfairshare.app.mapper.GroupMapper;
import com.payfairshare.app.models.Expense;
import com.payfairshare.app.models.Group;
import com.payfairshare.app.models.User;
import com.payfairshare.app.repository.GroupRepository;
import com.payfairshare.app.repository.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class GroupService{

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private ExpenseMapper expenseMapper;

   
    public ResponseEntity<Long> createGroup(Long userId, String name) {
        try{
            Group group = new Group();
            group.setName(name.substring(1, name.length()-1));
            List<User> userList = new ArrayList<>();
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                userList.add(user);
                group.setGroupUsers(userList);
                group = groupRepository.save(group);
                return ResponseEntity.ok(group.getId());
            }else{
                throw  new ResponseStatusException(HttpStatus.NOT_FOUND,"User doesn't exist");
            }
            
        }catch(ResponseStatusException e){
            throw new ResponseStatusException(e.getStatus(),e.getReason());
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }

    }

    public ResponseEntity<GroupDto> getGroupById(Long groupId) {
        try{
            Group group = groupRepository.findById(groupId).orElse(null);
            if(group != null){
                return ResponseEntity.ok(groupMapper.toGroupDto(group));
            }else{
                throw  new ResponseStatusException(HttpStatus.NOT_FOUND,"No such a group exists");
            }
            
        }catch(ResponseStatusException e){
            throw new ResponseStatusException(e.getStatus(), e.getReason());
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        
    }

    public ResponseEntity<GroupDto> addUser(Long grpId, Long userId) {
        Optional<Group> group = groupRepository.findById(grpId);
        if(group.isPresent()){
            List<User> usersToAdd = new ArrayList<>();
            User user = userRepository.findById(userId).orElse(null);
            if(user != null){
                usersToAdd.add(user);
                if(group.get().getGroupUsers().contains(user)){
                    throw new ResponseStatusException(HttpStatus.CONFLICT,"This user is already in the group.");
                }
                group.get().getGroupUsers().addAll(usersToAdd);
                groupRepository.save(group.get());
                return ResponseEntity.ok(groupMapper.toGroupDto(group.get()));  
            }else{
                throw  new ResponseStatusException(HttpStatus.NOT_FOUND,"User doesn't exists");
            }
            
       }else{
           throw  new ResponseStatusException(HttpStatus.NOT_FOUND,"Group doesn't exist");
       }
   }

   public ResponseEntity<List<GroupDto>> getUserGroups(Long userId) {
    try{
        User user = userRepository.findById(userId).get();
        if(user != null){
            List<Group> list = user.getUserGroups();
            if(list.size() != 0){
                 List<GroupDto> dtoList = new ArrayList<>();
                 for (Group group : list) {
                 GroupDto groupDto = new GroupDto();
                 groupDto.setId(group.getId());
                 groupDto.setName(group.getName());
                 groupDto.setGroupUsers(groupMapper.toUserNameDtos(group.getGroupUsers()));

            // Get the two most recent expenses for this group
                List<Expense> recentExpenses = group.getExpenses().stream()
                    .sorted(Comparator.comparing(Expense::getCreatedOn).reversed())
                    .limit(2)
                    .collect(Collectors.toList());

                List<ExpenseDto> expenseDtos = expenseMapper.toExpenseDtos(recentExpenses);

                groupDto.setExpenses(expenseDtos);

                // Add other properties to groupDto as needed

                dtoList.add(groupDto);
        }
                return ResponseEntity.ok(dtoList);
            }
            else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No groups found");
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


    public ResponseEntity<String> deleteGroup(Long groupId, Long userId) {
        try{
            Group group = groupRepository.findById(groupId).get();
            if(group == null){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Group not found");
                }
            if(userRepository.findById(userId).get().getUserGroups().contains(group)){
                groupRepository.deleteById(groupId);
                return ResponseEntity.ok("Deleted");
            }else{
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You are not a member in this group!");
            }
                
            }catch(ResponseStatusException e){
                throw new ResponseStatusException(e.getStatus(),e.getReason());
            }catch(Exception e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
            }
    }

    public ResponseEntity<String> exitGroup(Long groupId, Long userId){
         try{
            Group group = groupRepository.findById(groupId).get();
            if(group == null){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Group not found");
                }
            User user =  userRepository.findById(userId).get();
            if(user == null){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found");
                }
            if(user.getUserGroups().contains(group)){
                user.getUserGroups().remove(group);
                userRepository.save(user);
                return ResponseEntity.ok("Exited from group successfully!");
            }else{
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You are not a member in this group!");
            }
                
            }catch(ResponseStatusException e){
                throw new ResponseStatusException(e.getStatus(),e.getReason());
            }catch(Exception e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }
}
