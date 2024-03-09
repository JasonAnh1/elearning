package com.jason.elearning.entity.request;

import com.jason.elearning.entity.constants.QuizzType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class QuizzRequest implements Serializable{
    public  String qQuestion;
    public String qAnswer;
    public QuizzType qType;
    public String qA;
    public String qB;
    public String qC;
    public String qD;
}
