package com.project.user;

import java.net.URL;
import java.util.ResourceBundle;

import com.project.shared.AnimeCard;
import com.project.util.HoverAnimation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;

public class DiscoverController implements Initializable {
    @FXML
    private FlowPane root;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //FlowPane root = new FlowPane(10, 10);
        AnimeCard card1 = new AnimeCard(
            "Jujutsu Kaisen", "Mappa", "Fall 2020", "Oct 3, 2020",
            "a1.png", 24
        );



        AnimeCard card2 = new AnimeCard(
            "Attack on Titan", "Wit Studio", "Winter 2013", "Apr 7, 2013",
            "a1.png", 25
        );

        // animation
        HoverAnimation.install(card1);
        HoverAnimation.install(card2);

        root.getChildren().addAll(card1, card2);

    }
}
