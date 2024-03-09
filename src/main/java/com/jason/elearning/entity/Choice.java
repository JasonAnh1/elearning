package com.jason.elearning.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "choice")
@Getter
@Setter
public class Choice extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private long quizzId;
    private String content;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "quizzId", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private Quizz quizz;
}
