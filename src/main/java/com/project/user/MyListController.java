package com.project.user;
import com.project.navigation.Refreshable;
import java.net.URL;
import java.util.ResourceBundle;

import com.project.dto.TrackingCardDto;
import com.project.repository.AccountRepository;
import com.project.repository.AnimeRepository;
import com.project.repository.EpisodeRepository;
import com.project.repository.TrackingRepository;
import com.project.service.TrackingService;
import com.project.shared.TrackingCard;
import com.project.util.JpaUtil;

import jakarta.persistence.EntityManagerFactory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class MyListController implements Initializable, Refreshable {
    @FXML
    private BorderPane myListBorderPane;

    private MyListViewModel viewModel;
    // private ObservableList<TrackingCardDto> cardObservableList;
    public FlowPane myListFlowPane;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        EntityManagerFactory emf = JpaUtil.getEntityManagerFactory();
        viewModel = new MyListViewModel(new TrackingService(new TrackingRepository(emf),
                new AnimeRepository(emf), new AccountRepository(emf), new EpisodeRepository(emf)));

        myListFlowPane = new FlowPane(10, 10);
        myListFlowPane.setHgap(20);
        myListFlowPane.setVgap(20);
        myListFlowPane.setPrefWidth(800);
        myListFlowPane.setPrefHeight(600);
        myListFlowPane.setPadding(new Insets(20));

    }

    @Override
    public void onFresh() {
        myListFlowPane.getChildren().clear();
        for (TrackingCardDto cardDto : viewModel.getCardObservableList()) {
            TrackingCard card = new TrackingCard();
            card.setData(cardDto);
            myListFlowPane.getChildren().add(card);
        }

        myListBorderPane.setCenter(myListFlowPane);
        System.out.println("MyList Fresh");
    }

}
