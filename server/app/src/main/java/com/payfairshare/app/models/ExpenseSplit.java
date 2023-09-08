package com.payfairshare.app.models;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Table(name="expensesplits")
@Data
public class ExpenseSplit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;

    @Column(name = "user_responsible_amount")
    private Float userResponsibleAmount;

    @Column(name = "expense_id")
    private Long expenseId;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id", referencedColumnName = "id",insertable = false, updatable = false)
    private Expense expense;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "id",insertable = false, updatable = false)
    private User user;
    
    @Column(name="created_on")
    @CreationTimestamp
    private Timestamp createdOn;

	@Column(name = "updated_on")
    @UpdateTimestamp
    private Timestamp updatedOn;
}
