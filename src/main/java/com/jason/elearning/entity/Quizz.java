package com.jason.elearning.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jason.elearning.entity.constants.QuizzType;
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
    @Lob
    @Column(columnDefinition = "TEXT")
    private String question;
    private String answer;
    private QuizzType type;
    private long lessonId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "lessonId", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private Lesson lesson;
    @OneToMany(mappedBy = "quizz",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Choice> choices;



}
