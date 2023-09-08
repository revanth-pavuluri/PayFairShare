package com.payfairshare.app.models;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Data;

@Entity
@Data
@Table(name="expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Float amount;
    private String status;

    @Column(name = "paid_by_user_id")
    private Long userId;

    @Column(name = "group_id")
    private Long groupId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paid_by_user_id",referencedColumnName = "id",insertable = false, updatable = false)
    private User paidBy;

    
    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinTable(name = "user_expense",
            joinColumns = {@JoinColumn(name = "expense_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private List<User> usersInSplit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id",insertable = false, updatable = false)
    private Group group;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
    // @JoinColumn(name = "expense_split_id", referencedColumnName = "id")
    private List<ExpenseSplit> expenseSplits; 

    @Column(name="created_on")
    @CreationTimestamp
    private Timestamp createdOn;

	@Column(name = "updated_on")
    @UpdateTimestamp
    private Timestamp updatedOn;
}
