package com.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Animes")
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "anime_id")
    private Integer id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(name = "poster_url", length = 255)
    private String posterUrl;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String introduction;

    @Enumerated(EnumType.STRING)
    @Column(name = "anime_status", length = 15)
    private AnimeStatus animeStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "anime_type", length = 10)
    private AnimeType animeType;

    @Column(name = "total_episodes")
    private Short totalEpisodes;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_day", length = 10)
    private DayOfWeek scheduleDay; // Use DayOfWeek enum : Monday, Tuesday, ...

    @Column(name = "schedule_time")
    private LocalTime scheduleTime;

    // --- Relationships ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studio_id")
    private Studio studio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "season_name", referencedColumnName = "season_name"),
            @JoinColumn(name = "season_year", referencedColumnName = "season_year")
    })
    private Season season;

    @OneToMany(mappedBy = "anime", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Episode> episodes;

    @OneToMany(mappedBy = "anime", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Tracking> trackings;

    // @OneToMany(mappedBy = "anime", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // @EqualsAndHashCode.Exclude
    // @ToString.Exclude
    // private Set<Notification> notifications;

    public enum AnimeStatus {
        ONGOING, COMPLETED, HIATUS, CANCELLED, UPCOMING
    }

    public enum AnimeType {
        TV, MOVIE, OVA, SPECIAL
    }


}