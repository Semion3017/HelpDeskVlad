package com.bara.helpdesk.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
public class Attachment {

    @Id
    @Column(name = "ticket_id")
    private Long ticket_id;

    @Lob
    @Column(name = "blob", columnDefinition = "BLOB")
    private byte[] blob;

    @Column(name = "name")
    private String name;


}
