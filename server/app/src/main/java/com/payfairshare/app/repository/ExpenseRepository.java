package com.payfairshare.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.payfairshare.app.models.Expense;

@Repository
@Transactional
public interface ExpenseRepository extends JpaRepository<Expense,Long>{
    List<Expense> findByGroupId(Long id);
    List<Expense> findByGroupIdAndStatus(Long id, String status);
    Optional<Expense> findByIdAndUserId(Long id, Long userId);
}
