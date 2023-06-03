package com.bara.helpdesk.service;


import com.bara.helpdesk.dto.CommentInputDto;
import com.bara.helpdesk.dto.CommentOutputDto;

import java.util.List;

public interface CommentService {
    CommentOutputDto createComment(CommentInputDto dto, Long userId);

    List<CommentOutputDto> getAllByTicketId(Long id);
}
