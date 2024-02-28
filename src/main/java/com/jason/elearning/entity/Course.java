package com.jason.elearning.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "course")
@Getter
@Setter
public class Course extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    private long categoryId;
    private long authorId;
    private String title;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
    private String detail;
    private String shortDes;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String courseContent;
    private String level;
    private long price;
    private long priceSale;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String requirement;
    private int rating;
    private String advertise;
    private String status;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "categoryId", referencedColumnName = "id", insertable = false, updatable = false)
    private CourseCategory category;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "authorId", referencedColumnName = "id", insertable = false, updatable = false)
    private User author;
    @OneToMany(mappedBy = "course",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<CoursePart> courseParts;
}
