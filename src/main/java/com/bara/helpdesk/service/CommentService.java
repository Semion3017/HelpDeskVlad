package com.bara.helpdesk.service;


import com.bara.helpdesk.dto.CommentInputDto;
import com.bara.helpdesk.dto.CommentOutputDto;
import com.bara.helpdesk.dto.PageOutputDto;


public interface CommentService {
    CommentOutputDto createComment(CommentInputDto dto, Long userId);

    PageOutputDto<CommentOutputDto> getAllByTicketId(Long id, Integer page, Integer size);
}
