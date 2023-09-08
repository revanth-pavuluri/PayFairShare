package com.payfairshare.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payfairshare.app.dto.ExpenseSplitDto;
import com.payfairshare.app.service.ExpenseSplitService;

@RestController
@RequestMapping("/expensesplit")
@CrossOrigin(origins = "*")
public class ExpenseSplitController {
    @Autowired
    private ExpenseSplitService expenseSplitService;;

    @GetMapping("/{expId}")
    public ResponseEntity<List<ExpenseSplitDto>> getExpenseSplit(@PathVariable Long expID){
        return expenseSplitService.getExpenseSplit(expID);
    }

}
