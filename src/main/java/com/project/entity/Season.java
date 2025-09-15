package com.project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

import com.project.model.SeasonId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Seasons")
public class Season {

    @EmbeddedId
    private SeasonId id;

    // --- Relationships ---
    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude @ToString.Exclude
    private Set<Anime> animes;

    @Override
    public String toString() {
        return id.getSeasonName() + " " + id.getSeasonYear();
    }
}