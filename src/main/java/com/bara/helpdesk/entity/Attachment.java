package com.bara.helpdesk.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ATTACHMENTS")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long ticket_id;

    @Lob
    @Column(name = "blob", columnDefinition = "BLOB")
    private byte[] blob;

    @Column(name = "name")
    private String name;


}
