package com.jason.elearning.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "certificate")
@Getter
@Setter
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private long courseId;
    private long userId;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "courseId", referencedColumnName = "id", insertable = false, updatable = false)
    private Course course;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;
    private String code;
}
