package com.jason.elearning.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "quizz")
@Getter
@Setter
public class Quizz extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    private String question;
    private String answer;
    private String type;
    private long lessonId;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "lessonId", referencedColumnName = "id", insertable = false, updatable = false)
    private Lesson lesson;
    @OneToMany(mappedBy = "quizz",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Choice> choices;
}
