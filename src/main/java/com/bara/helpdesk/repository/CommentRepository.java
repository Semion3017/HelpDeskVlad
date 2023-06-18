package com.bara.helpdesk.repository;

import com.bara.helpdesk.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByTicketId(Long id);
    Page<Comment> findAllByTicketId(Long id, Pageable pageable);
}
