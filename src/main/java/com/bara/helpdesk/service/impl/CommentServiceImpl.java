package com.bara.helpdesk.service.impl;

import com.bara.helpdesk.dto.CommentInputDto;
import com.bara.helpdesk.dto.CommentOutputDto;
import com.bara.helpdesk.entity.Comment;
import com.bara.helpdesk.mapper.CommentMapper;
import com.bara.helpdesk.repository.CommentRepository;
import com.bara.helpdesk.repository.TicketRepository;
import com.bara.helpdesk.repository.UserRepository;
import com.bara.helpdesk.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    @Override
    public CommentOutputDto createComment(CommentInputDto dto, Long userId) {
        Comment comment = CommentMapper.toEntity(dto);
                comment.setUser((userRepository.findById(userId).orElseThrow()));
                comment.setTicket(ticketRepository.findById(dto.getTicketId()).orElseThrow());
        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentOutputDto> getAllByTicketId(Long id){
        return commentRepository.getAllByTicketId(id).stream().map(CommentMapper::toDto).collect(Collectors.toList());
    }
}
