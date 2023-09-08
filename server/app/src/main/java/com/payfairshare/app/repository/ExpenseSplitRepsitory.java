package com.payfairshare.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.payfairshare.app.models.ExpenseSplit;

@Repository
@Transactional
public interface ExpenseSplitRepsitory extends JpaRepository<ExpenseSplit,Long>{
    List<ExpenseSplit> findByExpenseId(Long id);
    List<ExpenseSplit> findByExpenseIdAndStatus(Long id, String status);
}
