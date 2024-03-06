package com.jason.elearning.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "course_part")
@Getter
@Setter
public class CoursePart extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private long courseId;
    private String title;
    private int partNumber;

//    @ManyToOne(cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "courseId", referencedColumnName = "id", insertable = false, updatable = false)
//    private Course course;

    @OneToMany(mappedBy = "part",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Lesson> lessons;


}
