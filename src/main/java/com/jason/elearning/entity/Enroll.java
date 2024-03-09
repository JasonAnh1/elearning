package com.jason.elearning.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jason.elearning.entity.constants.EnrollStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Enroll")
@Getter
@Setter
public class Enroll extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private long courseId;
    private long userId;
    private int progress;
    private EnrollStatus status;
    private String currentLessonPassed;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private User user;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "courseId", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private Course course;
}
