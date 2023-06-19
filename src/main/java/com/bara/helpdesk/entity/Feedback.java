package com.bara.helpdesk.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "FEEDBACKS")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "rate")
    private int rate;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "text", columnDefinition = "varchar(500)")
    private String text;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

}
