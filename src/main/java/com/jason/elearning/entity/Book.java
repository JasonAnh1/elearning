package com.jason.elearning.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "book")
@Getter
@Setter
public class Book extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private String title;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
    private long price;
    private long priceSale;
    private int rate;
    private String status;
    private String type;
    private long authorId;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "authorId", referencedColumnName = "id", insertable = false, updatable = false)
    private User author;

}
