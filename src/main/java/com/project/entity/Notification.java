package com.project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Integer id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_read", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean isRead;

    // --- Relationships ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Account account;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "anime_id")
    // private Anime anime;

    @Column(name = "notifiable_type")
    @Enumerated(EnumType.STRING)
    private NotifiableType notifiableType;

    @Column(name = "notifiable_id")
    private Long notifiableId; // can be episode_id, season_id, tracking_id

    public enum NotifiableType {
        EPISODE_RELEASE,
        NEW_SEASON,
        TRACKING_UPDATE,
        GENERAL_ANNOUNCEMENT
    }
}