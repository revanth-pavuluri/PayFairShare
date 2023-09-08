package com.payfairshare.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.payfairshare.app.dto.GroupDto;
import com.payfairshare.app.service.GroupService;

import java.util.List;

@RestController
@RequestMapping("/groups")
@CrossOrigin(origins = "*")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/{userId}/create")
    public ResponseEntity<Long> createGroup(@PathVariable Long userId,@RequestBody String groupname){
        return groupService.createGroup(userId,groupname);
    }

    @PostMapping("/adduser/{groupId}/{userId}")
    public ResponseEntity<GroupDto> addUser(@PathVariable Long groupId, @PathVariable Long userId){
        return groupService.addUser(groupId, userId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GroupDto>> getUserGroups(@PathVariable Long userId){
        return groupService.getUserGroups(userId);
    }

    @GetMapping("/get/{groupId}")
    public ResponseEntity<GroupDto> getGroupById(@PathVariable Long groupId ){
        return groupService.getGroupById(groupId);
    }

    @DeleteMapping("/delete/{groupId}/{userId}")
    public ResponseEntity<String> deleteGroup(@PathVariable Long groupId, @PathVariable Long userId){
        return groupService.deleteGroup(groupId,userId);
    }
    @GetMapping("/exit/{groupId}/{userId}")
    public ResponseEntity<String> exitGroup(@PathVariable Long groupId, @PathVariable Long userId){
        return groupService.exitGroup(groupId, userId);
    }
}
