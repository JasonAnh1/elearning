package com.jason.elearning.entity;

import com.jason.elearning.entity.constants.CourseLevel;
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
    @Lob
    @Column(columnDefinition = "TEXT")
    private String detail;
    private String shortDes;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String courseContent;
    private CourseLevel level;
    private long price;
    private long priceSale;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String requirement;
    private int rating;
    private String advertise;
    private String status;

    private Long mediaId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "mediaId", referencedColumnName = "id", insertable = false, updatable = false)
    private UploadFile media;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "categoryId", referencedColumnName = "id", insertable = false, updatable = false)
    private CourseCategory category;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "authorId", referencedColumnName = "id", insertable = false, updatable = false)
    private User author;

//    @OneToMany(mappedBy = "course",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
//    private Set<CoursePart> courseParts;
//

}
