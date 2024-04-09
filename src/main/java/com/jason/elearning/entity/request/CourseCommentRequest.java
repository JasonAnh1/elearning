package com.jason.elearning.entity.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseCommentRequest {
    private String content;
    private Long courseId;
    private int rate;
}
