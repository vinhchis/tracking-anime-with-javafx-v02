package com.project.user;

import java.net.URL;
import java.util.ResourceBundle;

import com.project.dto.AnimeCardDto;
import com.project.navigation.NavigationEvent;
import com.project.shared.AnimeCard;
import com.project.util.AlertUtil;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class DiscoverController implements Initializable {
    @FXML
    private BorderPane discoverPane;

    // filter

    // search by title


    // search by anime status

    // search by anime type

    // search by studio

    // search by season



    private DiscoverViewModel viewModel;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        viewModel = new DiscoverViewModel();

        // ------ UI setup ------ //
        FlowPane animeListFlowPane = new FlowPane(10, 10);
        animeListFlowPane.setHgap(20);
        animeListFlowPane.setVgap(20);
        animeListFlowPane.setPrefWidth(800);
        animeListFlowPane.setPrefHeight(600);
        animeListFlowPane.setPadding(new Insets(20));
        for (AnimeCardDto anime : viewModel.getAnimeCardDtos()) {
            AnimeCard card = new AnimeCard(
                anime.getTitle(),
                anime.getStudioName(),
                anime.getSeasonName() + " " + anime.getSeasonYear(),
                anime.getScheduleTime().toString(),
                anime.getScheduleDay().toString(),
                anime.getPosterUrl(),
                anime.getTotalEpisodes(),
                anime.getAnimeStatus(),
                anime.getAnimeType()
            );

            // add to tracking list when add button is clicked
            card.getAddBtn().setOnAction(e -> {
                System.out.println("Add button clicked for " + anime.getTitle());
                viewModel.addToList(anime.getAnimeId());
            });
            animeListFlowPane.getChildren().add(card);
        }
        discoverPane.setCenter(animeListFlowPane);

        // ------ UI setup ------ //

        // navigation
        viewModel.getNavigationEvent().addListener((obs, oldEvent, newEvent) -> {
            if(newEvent == NavigationEvent.FLAT_SUCCESS){
                 AlertUtil.showAlert(Alert.AlertType.INFORMATION,
                                discoverPane.getScene().getWindow(),
                                "Add Successful",
                                "You was added this anime to your tracking list!");
            }
             if(newEvent == NavigationEvent.FLAT_FAILURE){
                 AlertUtil.showAlert(Alert.AlertType.ERROR,
                                discoverPane.getScene().getWindow(),
                                "Add Failed",
                                "This anime is already in your tracking list.");
            }
            if(newEvent == NavigationEvent.FLAT_NEED_LOGIN){
                System.out.println("Need login to add to list.");
                 AlertUtil.showAlert(Alert.AlertType.INFORMATION,
                                discoverPane.getScene().getWindow(),
                                "Login Required",
                                "You need to login to add anime to your tracking list.");
            }

            // reset
            viewModel.getNavigationEvent().set(null);
        });

    }
}
