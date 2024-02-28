package com.jason.elearning.entity;


import com.jason.elearning.entity.constants.UploadFileType;


import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "upload_files")
public class UploadFile extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(max = 500)
    private String originUrl;

    @Size(max = 500)
    private String thumbUrl;

    @Enumerated(EnumType.STRING)
    private UploadFileType type;

    private Integer width;

    private Integer height;

    private Integer duration;

    private Long size;

    private String originName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public UploadFileType getType() {
        return type;
    }

    public void setType(UploadFileType type) {
        this.type = type;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }
}
