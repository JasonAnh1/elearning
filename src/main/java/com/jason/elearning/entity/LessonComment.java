package com.jason.elearning.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "lesson_comment")
@Getter
@Setter
public class LessonComment extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private String content;
    private Long lessonId;
    private Long userId;
    private Long parentCommentId;
    private String parentUserName;
    @ManyToOne(cascade = CascadeType.PERSIST )
    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

//    @OneToMany(mappedBy = "parentLessonComment",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @Transient
    private List<LessonComment> childrenComments;
    @Transient
    private boolean hiden = true;
}
