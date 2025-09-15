package com.project.user;

import com.project.dto.AnimeCardDto;
import com.project.entity.Account;
import com.project.entity.Anime;
import com.project.entity.Tracking;
import com.project.model.Session;
import com.project.navigation.NavigationEvent;
import com.project.repository.AccountRepository;
import com.project.repository.AnimeRepository;
import com.project.repository.EpisodeRepository;
import com.project.repository.SeasonRepository;
import com.project.repository.StudioRepository;
import com.project.repository.TrackingRepository;
import com.project.service.AnimeService;
import com.project.service.TrackingService;
import com.project.util.JpaUtil;

import jakarta.persistence.EntityManagerFactory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DiscoverViewModel {
    private ObservableList<AnimeCardDto> animes = FXCollections.observableArrayList();
    private ObjectProperty<NavigationEvent> navigationEvent = new SimpleObjectProperty<>(null);

    private final AnimeService animeService;
    private final TrackingService trackingService;
    private Session session = Session.getInstance();

    public DiscoverViewModel() {
        EntityManagerFactory emf = JpaUtil.getEntityManagerFactory();
        animeService = new AnimeService(new AnimeRepository(emf),
                new AccountRepository(emf),
                new SeasonRepository(emf),
                new StudioRepository(emf));
        trackingService = new TrackingService(new TrackingRepository(emf),
                new AnimeRepository(emf),
                new AccountRepository(emf), new EpisodeRepository(emf));

        animes.setAll(animeService.animeDtos());
    }

    public ObservableList<AnimeCardDto> getAnimeCardDtos() {
        return animes;
    }

    public void addToList(int animeId) {
        Account account = session.getCurrentAccount();
        if (account != null) {
            Tracking tracking = trackingService.addTracking(account.getId(), animeId);
            if(tracking == null) {
                System.out.println("Anime is already in your tracking list.");
                navigationEvent.set(NavigationEvent.FLAT_FAILURE);
            } else {
                System.out.println("Added to your tracking list.");
                navigationEvent.set(NavigationEvent.FLAT_SUCCESS);
            }
        } else {
            navigationEvent.set(NavigationEvent.FLAT_NEED_LOGIN);
        }
    }

    public ObjectProperty<NavigationEvent> getNavigationEvent() {
        return navigationEvent;
    }

}
