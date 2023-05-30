package com.bara.helpdesk.mapper;

import com.bara.helpdesk.dto.FeedbackInputDto;
import com.bara.helpdesk.dto.FeedbackOutputDto;
import com.bara.helpdesk.entity.Feedback;

import java.time.LocalDateTime;

public class FeedbackMapper {

    public static Feedback toEntity(FeedbackInputDto dto){
        return Feedback.builder()
                .text(dto.getText())
                .rate(dto.getRate())
                .date(LocalDateTime.now())
                .build();
    }

    public static FeedbackOutputDto toDto(Feedback feedback){
        return FeedbackOutputDto.builder()
                .rate(feedback.getRate())
                .text(feedback.getText())
                .build();
    }
}
