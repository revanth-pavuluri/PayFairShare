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
@Data
@Table(name="finalsplits")
public class FinalSplit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "pay_to")
    private Long payTo;
    @Column(name = "pay_by")
    private Long payBy;
	private Float amount;
    private String status;

    @Column(name = "group_id")
    private Long groupId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id",referencedColumnName = "id",insertable = false, updatable = false)
    private Group group;

    @Column(name="created_on")
    @CreationTimestamp
    private Timestamp createdOn;

	@Column(name = "updated_on")
    @UpdateTimestamp
    private Timestamp updatedOn;
}
