package com.project.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.project.navigation.NavigationEvent;
import com.project.navigation.SceneManaged;
import com.project.navigation.SceneManager;
import com.project.navigation.TabView;
import com.project.navigation.View;
import com.project.util.AlertUtil;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class AdminDashboardController implements SceneManaged  {
    private  SceneManager sceneManager;

    @FXML
    private Label helloLabel;

    @FXML
    private StackPane adminStackPane;

    // buttons
    @FXML
    private Button homeButton;
    @FXML
    private Button accountButton;
    @FXML
    private Button animeButton;
    @FXML
    private Button seasonButton;
    @FXML
    private Button studioButton;
    @FXML
    private Button episodeButton;

    // contents
    // @FXML
    // private Node homePane;
    // @FXML
    // private Node animePane;
    // @FXML
    // private Node accountPane;
    // @FXML
    // private Node seasonPane;
    // @FXML
    // private Node studioPane;
    // @FXML
    // private Node episodePane;

    private List<Button> navButtons;
    private final Map<Button, Node> paneCache = new HashMap<>();
    private AdminDashboardViewModel adminDashboardViewModel;

    @FXML
    public void initialize() {
        adminDashboardViewModel = new AdminDashboardViewModel();
        navButtons = List.of(homeButton, accountButton, animeButton, seasonButton, studioButton, episodeButton);

        loadShowPane(homeButton);
        setActiveButton(homeButton);

        adminDashboardViewModel.getUsernameProperty().bind(helloLabel.textProperty());

    }

    @FXML
    public void handleNavButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        loadShowPane(clickedButton);
        setActiveButton(clickedButton);
        setActiveButton(clickedButton);
    }


    public void logout(){
        adminDashboardViewModel.logout();
        if (adminDashboardViewModel.navigationRequestProperty().get() == NavigationEvent.NAVIGATE_TO_LOGIN) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION,
                                helloLabel.getScene().getWindow(),
                                "Register Successful",
                                "Back to the Login Page");
            sceneManager.switchTo(View.LOGIN);
        }
    }


    private void loadShowPane(Button button) {
       if (paneCache.containsKey(button)) {
            Node paneToShow = paneCache.get(button);
            System.out.println(button.getText() + " tab is loading");
            paneToShow.toFront();
        } else {
            String fxmlFile = getFxmlPathForButton(button).getFxmlFile();
            if (fxmlFile == null) return;

            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlFile)));
                Node newPane = loader.load();

                paneCache.put(button, newPane);
                adminStackPane.getChildren().add(newPane);
                System.out.println(button.getText() + " tab is loading");
                newPane.toFront();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void setActiveButton(Button activeButton) {
        for (Button button : navButtons) {
            button.getStyleClass().remove("active");
        }
        activeButton.getStyleClass().add("active");
    }

    private TabView getFxmlPathForButton(Button button) {
        if (button == homeButton) {
            return TabView.ADMIN_HOME;
        } else if (button == accountButton) {
            return TabView.ADMIN_ACCOUNT;
        } else if (button == animeButton) {
            return TabView.ADMIN_ANIME;
        } else if(button == studioButton){
            return TabView.ADMIN_STUDIO;
        }else if(button == seasonButton){
            return TabView.ADMIN_SEASON;
        }else if(button == episodeButton){
            return TabView.ADMIN_EPISODE;
        }
        return null;
    }


    @Override
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

}
