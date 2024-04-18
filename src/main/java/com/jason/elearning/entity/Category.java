package com.jason.elearning.entity;

import com.jason.elearning.entity.constants.CategoryType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "category")
@Getter
@Setter
public class Category extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    private CategoryType type;

}
