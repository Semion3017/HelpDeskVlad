package com.bara.helpdesk.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "rate")
    private int rate;

    @Column(name = "date")
    private Date date;

    @Column(name = "text", columnDefinition = "varchar(500)")
    private String text;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

}
