package com.jason.elearning.entity;

import com.jason.elearning.entity.constants.BookStatus;
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
    private BookStatus status;
    private String type;
    private long authorId;
    private long categoryId;
    private Long mediaId;
    private Long avatarId;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "authorId", referencedColumnName = "id", insertable = false, updatable = false)
    private User author;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "categoryId", referencedColumnName = "id", insertable = false, updatable = false)
    private Category category;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "mediaId", referencedColumnName = "id", insertable = false, updatable = false)
    private UploadFile media;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "avatarId", referencedColumnName = "id", insertable = false, updatable = false)
    private UploadFile avatar;
}
