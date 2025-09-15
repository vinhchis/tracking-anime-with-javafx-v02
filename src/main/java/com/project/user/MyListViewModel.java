package com.project.user;

import com.project.dto.TrackingCardDto;
import com.project.model.Session;
import com.project.service.TrackingService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MyListViewModel {
    private final TrackingService trackingService;
    private ObservableList<TrackingCardDto> cardObservableList = FXCollections.observableArrayList();
    private final Session session = Session.getInstance();

    public MyListViewModel(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    public ObservableList<TrackingCardDto> getCardObservableList() {
        int accountId = session.getCurrentAccount().getId();
        cardObservableList.setAll(trackingService.getAllDto(accountId));
        return cardObservableList;
    }
}
