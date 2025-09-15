package com.project.shared;

import com.project.dto.TrackingCardDto;
import com.project.entity.Tracking.TrackingStatus;
import com.project.util.AssetUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Rating;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TrackingCard extends VBox implements Initializable {
    @FXML
    private ImageView posterImageView;
    @FXML
    private Button deleteButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Label seasonLabel; // season year + name
    @FXML
    private Label studioLabel;
    @FXML
    private Label animeTypeLabel;
    @FXML
    private Label statusTagLabel;
    @FXML
    private Label currentEpisodeLabel;
    @FXML
    private Label episodeProgressLabel; // last watched episode / total episodes
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button increaseBtn;
    @FXML
    private Button decreaseBtn;

    @FXML
    private Rating rating;
    @FXML
    private ToggleButton noteToggleButton;
    @FXML
    private TextArea noteTextArea;

    @FXML
    private ComboBox<TrackingStatus> trackingStatusComboBox;

    // private final ObjectProperty<Consumer<Tracking>> onDeleteAction = new
    // SimpleObjectProperty<>();

    public TrackingCard() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/shared/TrackingCardView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load TrackingCardView.fxml", e);
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
         // FXMLLoader fxmlLoader = new
        // FXMLLoader(getClass().getResource("/com/project/shared/TrackingCardView.fxml"));
        // fxmlLoader.setController(this);
        // try {
        // VBox loadedRoot = fxmlLoader.load(); // don't call setRoot(this)
        // this.getChildren().setAll(loadedRoot.getChildren());
        // } catch (IOException exception) {
        // throw new RuntimeException(exception);
        // }
    }



    public void setData(TrackingCardDto trackingData) {
        posterImageView.setImage(AssetUtils.getImageFromComputer(trackingData.getImageUrl()));
        titleLabel.setText(trackingData.getAnimeTitle());
        seasonLabel.setText(trackingData.getSeasonName() + " " + trackingData.getSeasonYear());
        studioLabel.setText(trackingData.getStudioName());
        animeTypeLabel.setText(trackingData.getAnimeType());
        statusTagLabel.setText(trackingData.getAnimeStatus());
        currentEpisodeLabel.setText("Ep " + trackingData.getCurrentEpisode());

        episodeProgressLabel.setText(trackingData.getLastWatchedEpisode() + " / " + trackingData.getTotalEpisodes());
        if (trackingData.getTotalEpisodes() > 0) {
            progressBar.setProgress((double) trackingData.getLastWatchedEpisode() / trackingData.getTotalEpisodes());
        } else {
            progressBar.setProgress(0);
        }
        rating.setRating(trackingData.getRating());
        noteTextArea.setText(trackingData.getNote());

        // style
        noteTextArea.setWrapText(true);
        noteTextArea.setVisible(false);
        noteToggleButton.setSelected(false);
        noteToggleButton.setOnAction(e -> {
            noteTextArea.setVisible(noteToggleButton.isSelected());
        });

    }

    @FXML
    private void handleDelete() {
        // // 6. Khi nút xóa được nhấn, kích hoạt callback
        // if (onDeleteAction.get() != null) {
        // onDeleteAction.get().accept(trackingData);
        // }
    }



    // Cung cấp property để bên ngoài có thể set hành động xóa
    // public ObjectProperty<Consumer<Tracking>> onDeleteActionProperty() {
    // return onDeleteAction;
    // }
}
