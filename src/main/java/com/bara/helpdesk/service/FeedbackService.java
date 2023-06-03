package com.bara.helpdesk.service;

import com.bara.helpdesk.dto.FeedbackInputDto;
import com.bara.helpdesk.dto.FeedbackOutputDto;

public interface FeedbackService {

    FeedbackOutputDto create(FeedbackInputDto dto, Long userId);

    FeedbackOutputDto getByTicketId(Long id);
}
