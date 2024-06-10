package com.jason.elearning.entity.request;

import com.jason.elearning.entity.DateAudit;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "Message")
public class Message extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private String senderName;
    private String receiverName;
    private String message;
    private Status status;
}
