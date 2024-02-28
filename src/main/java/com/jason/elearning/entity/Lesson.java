package com.jason.elearning.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "lesson")
@Getter
@Setter
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private String title;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;
    private int passThreshold;
    private long coursePartId;
    private String type;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "coursePartId", referencedColumnName = "id", insertable = false, updatable = false)
    private CoursePart part;
}
