package com.payfairshare.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.payfairshare.app.dto.UserRequestDto;
import com.payfairshare.app.dto.UserResponseDto;
import com.payfairshare.app.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequestDto userRequest){
        System.out.println("entered");
        return userService.createUser(userRequest);
    }
    
    @GetMapping("/get/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId){
        return userService.getUserById(userId);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<UserResponseDto>> getGroupUsers(@PathVariable Long groupId){
        return userService.getGroupUsers(groupId);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        return userService.getAllUsers();
    }
    
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId){
        return userService.deleteUser(userId);
    }
}
