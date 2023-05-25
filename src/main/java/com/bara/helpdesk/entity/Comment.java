package com.bara.helpdesk.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "text", columnDefinition = "varchar(500)")
    private String string;

    @Column(name = "date")
    private Date date;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;




}
