package com.project.service;

import java.util.List;
import java.util.Optional;

import com.project.dto.TrackingCardDto;
import com.project.entity.Account;
import com.project.entity.Anime;
import com.project.entity.Season;
import com.project.entity.Studio;
import com.project.entity.Tracking;
import com.project.entity.Tracking.TrackingStatus;
import com.project.repository.AccountRepository;
import com.project.repository.AnimeRepository;
import com.project.repository.EpisodeRepository;

import com.project.repository.TrackingRepository;

public class TrackingService {
    private final TrackingRepository trackingRepository;
    private final AnimeRepository animeRepository;
    private final AccountRepository accountRepository;
    private final EpisodeRepository episodeRepository;

    public TrackingService(TrackingRepository trackingRepository, AnimeRepository animeRepository,
            AccountRepository accountRepository, EpisodeRepository episodeRepository) {
        this.trackingRepository = trackingRepository;
        this.animeRepository = animeRepository;
        this.accountRepository = accountRepository;
        this.episodeRepository = episodeRepository;
    }

    public void deleteTracking(int id) {
        trackingRepository.deleteById(id);
    }

    public Tracking addTracking(int accountId, int animeId) {
        Optional<Tracking> oldTrackingOpt = trackingRepository.findByAccountIdAndAnimeId(accountId, animeId);
        if (!oldTrackingOpt.isPresent()) {
            Optional<Account> accountOpt = accountRepository.findById(accountId);
            Optional<Anime> animeOpt = animeRepository.findById(animeId);
            if (accountOpt.isPresent() && animeOpt.isPresent()) {
                Tracking tracking = new Tracking();
                tracking.setAccount(accountOpt.get());
                tracking.setAnime(animeOpt.get());

                // properties
                tracking.setTrackingStatus(TrackingStatus.WATCHING);
                tracking.setRating((byte) 5);
                tracking.setLastWatchedEpisode((short) 0);
                tracking.setNote("");

                return trackingRepository.save(tracking);
            }
        }
        return null;
    }

    public List<TrackingCardDto> getAllDto(int accountId) {
        return trackingRepository.findAllByAccountId(accountId).stream()
                .map(this::mapperToDto)
                .toList();
    }

    private TrackingCardDto mapperToDto(Tracking tracking) {
        TrackingCardDto dto = new TrackingCardDto();
        // tracking
        dto.setTrackingId(tracking.getId());
        dto.setTrackingStatus(tracking.getTrackingStatus().toString());
        dto.setAnimeTitle(tracking.getAnime().getTitle());
        dto.setLastWatchedEpisode(tracking.getLastWatchedEpisode());
        dto.setRating(tracking.getRating());
        dto.setNote(tracking.getNote());

        // anime
        Anime anime = tracking.getAnime();
        dto.setAnimeId(anime.getId());
        dto.setAnimeStatus(anime.getAnimeStatus().toString());
        dto.setAnimeType(anime.getAnimeType().toString());
        dto.setImageUrl(anime.getPosterUrl());
        dto.setTotalEpisodes(anime.getTotalEpisodes());

        // season
        Season season = anime.getSeason();
        if (season != null) {
            dto.setSeasonName(season.getId().getSeasonName());
            dto.setSeasonYear(season.getId().getSeasonYear());
        } else {
            dto.setSeasonName("Unknown");
            dto.setSeasonYear((short) 0);
        }
        // studio
        Studio studio = anime.getStudio();
        if (studio != null) {
            dto.setStudioName(studio.getStudioName());
        } else {
            dto.setStudioName("Unknown");
        }
        // episode get by anime id from episode service
        short currentEp = episodeRepository.countByAnimeId(anime.getId());
        dto.setCurrentEpisode(currentEp);
        return dto;
    }

}
