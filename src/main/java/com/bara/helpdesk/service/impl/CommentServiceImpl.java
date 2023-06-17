package com.bara.helpdesk.service.impl;

import com.bara.helpdesk.dto.CommentInputDto;
import com.bara.helpdesk.dto.CommentOutputDto;
import com.bara.helpdesk.entity.Comment;
import com.bara.helpdesk.mapper.CommentMapper;
import com.bara.helpdesk.repository.CommentRepository;
import com.bara.helpdesk.service.CommentService;
import com.bara.helpdesk.service.TicketService;
import com.bara.helpdesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


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
    public List<CommentOutputDto> getAllByTicketId(Long id) {
        return commentRepository.getAllByTicketId(id).stream().map(CommentMapper::toDto).collect(Collectors.toList());
    }
}
