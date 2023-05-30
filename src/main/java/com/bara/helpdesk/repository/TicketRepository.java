package com.bara.helpdesk.repository;

import com.bara.helpdesk.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {


    @Query("SELECT t FROM Ticket t WHERE t.owner.id = ?1 or t.approver.id = ?1 or t.assignee.id = ?1")
    List<Ticket> findByUserId(Long userId);
}
