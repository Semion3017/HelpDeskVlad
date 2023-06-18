package com.bara.helpdesk.repository;

import com.bara.helpdesk.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findAllByTicketId(Long id);

    Page<History> findAllByTicketId(Long id, Pageable pageable);
}
