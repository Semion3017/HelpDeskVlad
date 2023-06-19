package com.bara.helpdesk.repository;

import com.bara.helpdesk.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Optional<Feedback> getByTicketId(Long id);

    Boolean existsByTicketId(Long ticketId);
}
