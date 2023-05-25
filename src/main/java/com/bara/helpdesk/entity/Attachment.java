package com.bara.helpdesk.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
public class Attachment {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "blob", columnDefinition = "BLOB")
    private byte[] blob;

    @OneToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(name = "name")
    private String name;


}
