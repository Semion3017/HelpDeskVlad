package com.bara.helpdesk.repository;

import com.bara.helpdesk.entity.Ticket;
import com.bara.helpdesk.entity.enums.Urgency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {

//    @Query("SELECT t FROM Ticket t WHERE ?1 is null or (upper(t.name) LIKE concat('%', upper(?1), '%') " +
//            "or upper(cast(t.urgency as java.lang.String)) LIKE concat('%', upper(?1), '%') " +
//            "or upper(cast(t.desiredResolutionDate as java.lang.String)) LIKE concat('%', upper(?1), '%')) " +
//            "or upper(cast(t.state as java.lang.String)) LIKE concat('%', upper(?1), '%')")
//    Page<Ticket> findAllSortedByKeyword(Pageable pageable, String keyword);
    @Query("SELECT t FROM Ticket t WHERE t.owner.id = ?1 or t.approver.id = ?1 or t.assignee.id = ?1")
    List<Ticket> findByUserId(Long userId);
}
