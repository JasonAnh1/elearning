package com.jason.elearning.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jason.elearning.entity.constants.LessonType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "lesson")
@Getter
@Setter
public class Lesson extends DateAudit {
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
    private LessonType type;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "coursePartId", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private CoursePart part;
    private int position;
    private Long mediaId;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "mediaId", referencedColumnName = "id", insertable = false, updatable = false)
    private UploadFile media;
    @Transient
    private boolean isLock;


}
