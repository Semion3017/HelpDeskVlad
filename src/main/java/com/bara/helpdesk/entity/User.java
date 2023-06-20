package com.bara.helpdesk.entity;

import com.bara.helpdesk.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name", columnDefinition = "varchar(100)")
    private String firstName;

    @Column(name = "last_name", columnDefinition = "varchar(100)")
    private String lastName;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(name = "email", unique = true, columnDefinition = "varchar(100)")
    @Email
    private String email;

    @Column(name = "password")
    private String password;

}
