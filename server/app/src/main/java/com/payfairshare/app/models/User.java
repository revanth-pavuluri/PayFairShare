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
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Table(name="users")
@Entity
@Data
public class User{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String name;
  private String password;

  @Column(name = "upi_id")
  private String upiId;

    @ManyToMany(cascade = CascadeType.MERGE,mappedBy = "groupUsers",fetch = FetchType.EAGER)
    // @JoinColumn(name = "group_id" , referencedColumnName = "id")
    @JsonIgnore
    private List<Group> userGroups;

    @ManyToMany(cascade = CascadeType.MERGE, mappedBy = "usersInSplit",fetch = FetchType.LAZY)
    // @JoinColumn(name = "expense_id" , referencedColumnName = "id")
    @JsonIgnore
    private List<Expense> expenses;

    @Column(name="created_on")
    @CreationTimestamp
    private Timestamp createdOn;

	@Column(name = "updated_on")
    @UpdateTimestamp
    private Timestamp updatedOn;
    
}
