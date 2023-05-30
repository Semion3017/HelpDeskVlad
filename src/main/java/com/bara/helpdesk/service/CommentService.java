package com.bara.helpdesk.service;


import com.bara.helpdesk.dto.CommentInputDto;
import com.bara.helpdesk.dto.CommentOutputDto;

public interface CommentService {
    CommentOutputDto createComment(CommentInputDto dto, Long userId);
}
