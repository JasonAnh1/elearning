package com.jason.elearning.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "course_comments")
@Getter
@Setter
public class CourseComment extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private long enrollId;
    private double rate;
    private int liked;
    private int disliked;
    private String content;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "enrollId", referencedColumnName = "id", insertable = false, updatable = false)
    private Enroll enroll;

}
