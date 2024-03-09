package com.jason.elearning.entity.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class QuizzesRequest  {
    List<QuizzRequest> quizzList;
    long lessonId;
}
