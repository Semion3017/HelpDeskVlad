package com.bara.helpdesk.service.impl;

import com.bara.helpdesk.dto.HistoryOutputDto;
import com.bara.helpdesk.entity.History;
import com.bara.helpdesk.entity.Ticket;
import com.bara.helpdesk.entity.User;
import com.bara.helpdesk.entity.enums.State;
import com.bara.helpdesk.repository.HistoryRepository;
import com.bara.helpdesk.repository.UserRepository;
import com.bara.helpdesk.service.HistoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;

    @Override
    public List<HistoryOutputDto> getByTicketId(Long ticketId){
        return historyRepository.getAllByTicketId(ticketId).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public String logTicketCreation(Ticket ticket) {
        History history = History.builder()
                .ticket(ticket)
                .user(ticket.getOwner())
                .action("Ticket is created")
                .date(LocalDateTime.now())
                .description("Ticket is created")
                .build();
        return historyRepository.save(history).getDescription();
    }

    @Override
    public String logTicketUpdate(Ticket ticket) {
        History history = History.builder()
                .ticket(ticket)
                .user(ticket.getOwner())
                .action("Ticket is created")
                .date(LocalDateTime.now())
                .description("Ticket is created")
                .build();
        return historyRepository.save(history).getDescription();
    }

    @Override
    public String logStateChange(State oldState, Ticket ticket, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        History history = History.builder()
                .user(user)
                .action("Ticket state is changed")
                .date(LocalDateTime.now())
                .description(String.format("Ticket state is changed from %s to %s", oldState.name(), ticket.getState().name()))
                .ticket(ticket)
                .build();
        return historyRepository.save(history).getDescription();
    }

    private HistoryOutputDto toDto(History history){
        return HistoryOutputDto.builder()
                .email(history.getUser().getEmail())
                .action(history.getAction())
                .date(history.getDate())
                .description(history.getDescription())
                .build();
    }

}
