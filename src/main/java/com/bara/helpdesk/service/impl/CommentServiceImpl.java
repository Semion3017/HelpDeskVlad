package com.bara.helpdesk.service.impl;

import com.bara.helpdesk.dto.CommentInputDto;
import com.bara.helpdesk.dto.CommentOutputDto;
import com.bara.helpdesk.dto.PageOutputDto;
import com.bara.helpdesk.entity.Comment;
import com.bara.helpdesk.mapper.CommentMapper;
import com.bara.helpdesk.repository.CommentRepository;
import com.bara.helpdesk.service.CommentService;
import com.bara.helpdesk.service.TicketService;
import com.bara.helpdesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final TicketService ticketService;

    @Override
    public CommentOutputDto createComment(CommentInputDto dto, Long userId) {
        Comment comment = CommentMapper.toEntity(dto);
        comment.setUser((userService.getById(userId)));
        comment.setTicket(ticketService.getById(dto.getTicketId()));
        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public PageOutputDto<CommentOutputDto> getAllByTicketId(Long id, Integer page, Integer size) {
        Integer count = commentRepository.findAllByTicketId(id).size();
        Page<CommentOutputDto> commentPage = commentRepository.findAllByTicketId(id, PageRequest.of(page - 1, size)).map(CommentMapper::toDto);
        return new PageOutputDto<>(commentPage.getContent(), count);
    }
}
