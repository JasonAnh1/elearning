package com.jason.elearning.entity.request;

import com.jason.elearning.entity.constants.QuizzType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UpdateQuestionRequest implements Serializable {
    public Long qId;
    public String qQuestion;
    public String qAnswer;
    public QuizzType qType;
    public UpdateChoiceRequest qA;
    public UpdateChoiceRequest qB;
    public UpdateChoiceRequest qC;
    public UpdateChoiceRequest qD;
    public Boolean removeFlag;
    public Long lessonId;
}
