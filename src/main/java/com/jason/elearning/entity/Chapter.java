package com.jason.elearning.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "chapter")
@Getter
@Setter
public class Chapter extends DateAudit
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private String title;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;
    private long bookId;
    private int chapNumber;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "bookId", referencedColumnName = "id", insertable = false, updatable = false)
    private Book book;
}
