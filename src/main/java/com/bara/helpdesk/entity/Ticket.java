package com.bara.helpdesk.entity;

import com.bara.helpdesk.entity.enums.State;
import com.bara.helpdesk.entity.enums.Urgency;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "TICKETS")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "varchar(500)")
    private String description;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "desired_resolution_date")
    private LocalDate desiredResolutionDate;

    @OneToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @OneToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Enumerated(EnumType.STRING)
    private State state;

    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    private Urgency urgency;

    @Column(name = "urgency_number")
    private Integer urgencyNumber;

    @OneToOne
    @JoinColumn(name = "approver_id")
    private User approver;

    @OneToOne
    private Attachment attachment;
}
