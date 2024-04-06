package com.jason.elearning.entity.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class QuizzesRequest  implements Serializable {
    List<QuizzRequest> quizzList;
    long lessonId;
}
