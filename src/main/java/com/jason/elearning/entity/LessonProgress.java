package com.jason.elearning.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "lesson_progress")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LessonProgress extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private Long userId;
    private Long lessonId;
    private Double progress;
    private Double videoProgress;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
    private User learner;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "lessonId", referencedColumnName = "id", insertable = false, updatable = false)
    private Lesson lesson;
    private Boolean locked;
}
