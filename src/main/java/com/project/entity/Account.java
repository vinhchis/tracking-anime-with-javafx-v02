package com.project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer id;

    @Column(length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "hashed_password", length = 60, nullable = false)
    private String hashedPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", length = 10)
    private Role userRole;

    // --- Relationships ---
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude @ToString.Exclude
    private Set<Tracking> trackings;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude @ToString.Exclude
    private Set<Notification> notifications;

    public enum Role {
        USER, ADMIN
    }
}