package com.jason.elearning.entity;

import com.jason.elearning.entity.constants.PlanType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "plan")
@Getter
@Setter
public class Plan extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private Long organizationId;
    private PlanType type;
    @ManyToOne(cascade = CascadeType.PERSIST )
    @JoinColumn(name = "organizationId", referencedColumnName = "id", insertable = false, updatable = false)
    private User organization;

}
