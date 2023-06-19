package com.bara.helpdesk.service.impl;

import com.bara.helpdesk.dto.FeedbackInputDto;
import com.bara.helpdesk.dto.FeedbackOutputDto;
import com.bara.helpdesk.dto.exception.FeedbackNotFoundException;
import com.bara.helpdesk.dto.exception.IllegalActionException;
import com.bara.helpdesk.entity.Feedback;
import com.bara.helpdesk.entity.Ticket;
import com.bara.helpdesk.entity.enums.State;
import com.bara.helpdesk.mapper.FeedbackMapper;
import com.bara.helpdesk.repository.FeedbackRepository;
import com.bara.helpdesk.service.FeedbackService;
import com.bara.helpdesk.service.MailService;
import com.bara.helpdesk.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final MailService mailService;
    private final TicketService ticketService;

    @Override
    public FeedbackOutputDto create(FeedbackInputDto dto, Long userId) {
        Feedback feedback = FeedbackMapper.toEntity(dto);
        Ticket ticket = ticketService.getById(dto.getTicketId());
        if (!Objects.equals(userId, ticket.getOwner().getId()) || State.DONE != ticket.getState()) {
            throw new IllegalActionException();
        }
        if (feedbackRepository.existsByTicketId(ticket.getId())) {
            throw new IllegalActionException("Feedback already exists");
        }
        feedback.setTicket(ticket);
        feedback.setUser(ticket.getOwner());
        FeedbackOutputDto savedFeedback = FeedbackMapper.toDto(feedbackRepository.save(feedback));
        mailService.sendFeedbackProvidedMessage(ticket);
        return savedFeedback;
    }

    @Override
    public FeedbackOutputDto getByTicketId(Long id) {
        return FeedbackMapper.toDto(feedbackRepository.getByTicketId(id)
                .orElseThrow(() -> new FeedbackNotFoundException("Feedback with ID: " + id + " not found")));
    }

}