package com.jason.elearning.entity.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public class AnswerSheetRequest implements Serializable {
        public Long lessonId;

        public List<AnswerRequest> answers;
    }
