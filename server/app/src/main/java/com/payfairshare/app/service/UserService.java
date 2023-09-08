package com.payfairshare.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import com.payfairshare.app.dto.UserRequestDto;
import com.payfairshare.app.dto.UserResponseDto;
import com.payfairshare.app.mapper.UserMapper;
import com.payfairshare.app.models.Group;
import com.payfairshare.app.models.User;
import com.payfairshare.app.repository.GroupRepository;
import com.payfairshare.app.repository.UserRepository;


@Service
public class UserService {
	 @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;
  

    
    public ResponseEntity<String> deleteUser(Long userId) {
        try{
            User user = userRepository.findById(userId).get();
                if(user == null){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id not found");
                }
                userRepository.deleteById(userId);
                return ResponseEntity.ok("Deleted");
        }catch(ResponseStatusException e){
            throw new ResponseStatusException(e.getStatus(),e.getReason());
        }catch(Exception e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }
  
    public ResponseEntity<String> createUser(UserRequestDto userDto) {
        try{
            if(userRepository.findByUsername(userDto.getUsername()) != null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username already exists");
            }
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            User user = userMapper.userRequestDtoToUser(userDto);
            userRepository.save(user);
            return ResponseEntity.ok("created");
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        
    }

    public ResponseEntity<User> loadUserByUserName(String userName) {
        try{
            return ResponseEntity.ok(userRepository.findByUsername(userName));
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        
    }
   
    public ResponseEntity<UserResponseDto> getUserById(Long userId) {
        try{
            return ResponseEntity.ok(userMapper.userToUserResponseDto(userRepository.findById(userId).get()));
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

        public ResponseEntity<List<UserResponseDto>> getGroupUsers(Long groupId) {
        try{
            Group group = groupRepository.findById(groupId).orElse(null);
            if(group != null){
                List<User> list = group.getGroupUsers();
                if(list.size() != 0){
                    return ResponseEntity.ok(userMapper.userToUserResponseDtos(list));
                }
                else{
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No users found");
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

    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        try{
            return ResponseEntity.ok(userMapper.userToUserResponseDtos(userRepository.findAll()));
        }catch(Exception e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());}
    }
}
