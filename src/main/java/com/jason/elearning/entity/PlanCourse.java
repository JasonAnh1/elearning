package com.jason.elearning.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "plan_course")
@Getter
@Setter
public class PlanCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private Long planId;
    private Long courseID;
    @ManyToOne(cascade = CascadeType.PERSIST )
    @JoinColumn(name = "planId", referencedColumnName = "id", insertable = false, updatable = false)
    private Plan plan;
}
