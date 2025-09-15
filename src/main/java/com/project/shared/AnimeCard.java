package com.project.shared;

import com.project.util.AssetUtils;
import com.project.util.HoverAnimation;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;

public class AnimeCard extends VBox {
    private final Label titleLabel;
    private final Label studioLabel;
    private final Label seasonLabel;
    private final Label timeLabel;
    private final Label dateLabel;
    private final Label epsLabel;
    private final Button addBtn;
    private final Label typeLabel;
    private final Label statusLabel;

    public AnimeCard(String title, String studio, String season, String time, String date,
                     String imageUrl, int eps, String status, String type) {

        // ===== Card style =====
        this.setPrefWidth(250);
        this.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);"
        );

        // ===== Image section =====
        StackPane imagePane = new StackPane();
        // Image posterImage = AssetUtils.getImage(imageUrl);
        Image posterImage = AssetUtils.getImageFromComputer(imageUrl);

        ImageView poster = new ImageView(posterImage);
        poster.setFitWidth(250);
        poster.setFitHeight(300);
        poster.setPreserveRatio(false);

        // Badges
        typeLabel = new Label(status);
        typeLabel.setStyle("-fx-background-color: #eee; -fx-padding: 3 8; -fx-background-radius: 12;");
        statusLabel = new Label(type);
        statusLabel.setStyle("-fx-background-color: #eee; -fx-padding: 3 8; -fx-background-radius: 12;");

        HBox badges = new HBox(5, typeLabel, statusLabel);
        badges.setAlignment(Pos.TOP_LEFT);
        badges.setPadding(new Insets(10));

        Button addBtnTop = new Button("+");
        addBtnTop.setStyle(
            "-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 50%;"
        );
        StackPane.setAlignment(addBtnTop, Pos.TOP_RIGHT);
        StackPane.setMargin(addBtnTop, new Insets(10));

        imagePane.getChildren().addAll(poster, badges, addBtnTop);

        // ===== Info section =====
        VBox infoBox = new VBox(5);
        infoBox.setPadding(new Insets(10));

        titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        studioLabel = new Label("🎬 " + studio);
        seasonLabel = new Label("🍂 " + season);
        timeLabel = new Label("⏰ " + time);
        dateLabel = new Label("📅 " + date);

        epsLabel = new Label(eps + " eps");
        epsLabel.setStyle("-fx-font-weight: bold;");

        infoBox.getChildren().addAll(titleLabel, studioLabel, seasonLabel, timeLabel, dateLabel, epsLabel);

        // ===== Add button bottom =====
        addBtn = new Button("+ Add");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setStyle(
            "-fx-background-color: black; -fx-text-fill: white; -fx-font-weight: bold;" +
            "-fx-background-radius: 8;"
        );
        VBox.setMargin(addBtn, new Insets(0, 10, 10, 10));

        // ===== Combine =====
        this.getChildren().addAll(imagePane, infoBox, addBtn);

        // Animation
    }

    public Button getAddBtn() {
        return addBtn;
    }
}


