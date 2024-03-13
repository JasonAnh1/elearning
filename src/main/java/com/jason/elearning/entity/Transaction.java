package com.jason.elearning.entity;

import com.jason.elearning.entity.constants.TransactionStatus;
import com.jason.elearning.entity.constants.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private long amount;
    private String description;
    private long sender_id;
    private String receiver;
    private String remiters;
    private TransactionStatus status;
    private TransactionType type;
    private String transCode;
}
