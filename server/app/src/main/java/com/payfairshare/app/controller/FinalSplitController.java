package com.payfairshare.app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.payfairshare.app.dto.FinalSplitDto;
import com.payfairshare.app.service.FinalSplitService;

import java.util.List;

@RestController
@RequestMapping("/finalsplit")
@CrossOrigin(origins = "*")
public class FinalSplitController {
    @Autowired
    private FinalSplitService finalSplitService;

    @GetMapping("/do/{groupId}")
    public ResponseEntity<List<FinalSplitDto>> getFinalSplit(@PathVariable Long groupId){
        return finalSplitService.getFinalSplit(groupId);
    }
    @GetMapping("/get/{groupId}")
    public ResponseEntity<List<FinalSplitDto>> getGroupFinal(@PathVariable Long groupId){
        return finalSplitService.getGroupFinal(groupId);
    }
    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateStatus(@RequestBody FinalSplitDto finalSplitDto, @PathVariable Long userId){
        return finalSplitService.updateStatus(finalSplitDto, userId);
    }

}
