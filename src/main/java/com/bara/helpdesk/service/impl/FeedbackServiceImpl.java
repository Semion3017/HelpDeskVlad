package com.bara.helpdesk.service.impl;

import com.bara.helpdesk.dto.FeedbackInputDto;
import com.bara.helpdesk.dto.FeedbackOutputDto;
import com.bara.helpdesk.entity.Feedback;
import com.bara.helpdesk.entity.Ticket;
import com.bara.helpdesk.mapper.FeedbackMapper;
import com.bara.helpdesk.repository.FeedbackRepository;
import com.bara.helpdesk.repository.TicketRepository;
import com.bara.helpdesk.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final TicketRepository ticketRepository;

    @Override
    public FeedbackOutputDto create(FeedbackInputDto dto, Long userId) {
        Feedback feedback = FeedbackMapper.toEntity(dto);
        Ticket ticket = ticketRepository.findById(dto.getTicketId()).orElseThrow();
        if (!Objects.equals(userId, ticket.getOwner().getId())){
            throw  new RuntimeException("Invalid credentials");
            //TODO controllerAdvice
        }
        feedback.setTicket(ticket);
        feedback.setUser(ticket.getOwner());
        return FeedbackMapper.toDto(feedbackRepository.save(feedback));
    }

    @Override
    public FeedbackOutputDto getByTicketId(Long id){
        return FeedbackMapper.toDto(feedbackRepository.getByTicketId(id).orElseThrow());
    }

}