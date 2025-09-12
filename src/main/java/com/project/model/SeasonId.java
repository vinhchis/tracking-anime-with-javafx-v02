package com.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SeasonId implements Serializable {
    @Column(name = "season_name", length = 10, nullable = false)
    private String seasonName;

    @Column(name = "season_year", nullable = false)
    private Short seasonYear;

    public SeasonId(String winter, short i) {
    }
}