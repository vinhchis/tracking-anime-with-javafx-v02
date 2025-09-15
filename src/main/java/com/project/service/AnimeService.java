package com.project.service;

import java.util.List;

import com.project.dto.AnimeCardDto;
import com.project.entity.Anime;
import com.project.repository.AccountRepository;
import com.project.repository.AnimeRepository;
import com.project.repository.SeasonRepository;
import com.project.repository.StudioRepository;

public class AnimeService {
    private final AnimeRepository animeRepository;
    private final AccountRepository accountRepository;
    private final SeasonRepository seasonRepository;
    private final StudioRepository studioRepository;

    public AnimeService(AnimeRepository animeRepository, AccountRepository accountRepository,
            SeasonRepository seasonRepository, StudioRepository studioRepository) {
        this.animeRepository = animeRepository;
        this.accountRepository = accountRepository;
        this.seasonRepository = seasonRepository;
        this.studioRepository = studioRepository;
    }

    public List<AnimeCardDto> animeDtos() {
        return animeRepository.findAllWithRelations().stream().map(this::mapToDto).toList();
    }

    public void deleteAnime(Anime anime) {
        animeRepository.delete(anime);
    }

    public int count() {
        return (int) animeRepository.count();
    }

    private AnimeCardDto mapToDto(Anime anime) {
        AnimeCardDto dto = new AnimeCardDto();
        // season, studio can be null
        dto.setAnimeId(anime.getId());
        dto.setTitle(anime.getTitle());
        dto.setPosterUrl(anime.getPosterUrl());
        dto.setAnimeStatus(anime.getAnimeStatus().name());
        dto.setAnimeType(anime.getAnimeType().name());


        // can be null
        dto.setTotalEpisodes(anime.getTotalEpisodes());
        dto.setScheduleDay(anime.getScheduleDay() == null ? "TBA" : anime.getScheduleDay().toString());
        dto.setScheduleTime(anime.getScheduleTime() == null ? "TBA" : anime.getScheduleTime().toString());

        if (anime.getSeason() == null) {
            dto.setSeasonName("Unknown");
            dto.setSeasonYear(0);
        } else {
            dto.setSeasonName(anime.getSeason().getId().getSeasonName());
            dto.setSeasonYear(anime.getSeason().getId().getSeasonYear());
        }

        if (anime.getStudio() == null) {
            dto.setStudioName("Unknown");
            dto.setStudioId(0);
        } else {
            dto.setStudioName(anime.getStudio().getStudioName());
            dto.setStudioId(anime.getStudio().getId());
        }

        return dto;
    }
}
