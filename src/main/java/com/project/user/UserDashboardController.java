package com.project.user;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import com.project.entity.Account;
import com.project.navigation.NavigationEvent;
import com.project.navigation.Refreshable;
import com.project.navigation.SceneManaged;
import com.project.navigation.SceneManager;
import com.project.navigation.UserTabView;
import com.project.navigation.View;
import com.project.util.AlertUtil;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class UserDashboardController implements SceneManaged, Initializable, Refreshable {
    @FXML
    private BorderPane userDashboardBP;
    // tabs
    @FXML
    private Button overviewButton;
    @FXML
    private Button myListButton;
    @FXML
    private Button discoverButton;
    @FXML
    private StackPane userStackPane;

    private List<Button> navButtons;
    private final Map<Button, Node> paneCache = new HashMap<>();
    // cache controllers so we can call lifecycle hooks (onFresh) later
    private final Map<Button, Object> controllerCache = new HashMap<>();

    // auth page
    @FXML
    private StackPane authStackPane;
    @FXML
    private Node loggedInPane;
    @FXML
    private Node loggedOutPane;

    @FXML
    private Label helloLabel;

    private UserDashboardViewModel userDashboardViewModel;
    private SceneManager sceneManager;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        userDashboardViewModel = new UserDashboardViewModel();

        // nav buttons
        navButtons = List.of(overviewButton, myListButton, discoverButton);

        navButtons.forEach(button -> button.setOnAction(this::handleNavButtonClick));
        loadShowPane(discoverButton);
        setActiveButton(discoverButton);

        // allow stack pane to expand and follow parent size
        userStackPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        // bind stack pane to the dashboard borderpane so it gets full available space
        userStackPane.prefWidthProperty().bind(userDashboardBP.widthProperty());
        userStackPane.prefHeightProperty().bind(userDashboardBP.heightProperty());

        // bindings
        helloLabel.textProperty().bind(Bindings.createStringBinding(
                () -> {
                    ObjectProperty<Account> accProp = userDashboardViewModel.getCurrentUser();
                    Account acc = accProp != null ? accProp.getValue() : null;
                    return "Hello, " + (acc != null ? acc.getUsername() : "Guest");
                },
                userDashboardViewModel.getCurrentUser()));

        loggedInPane.visibleProperty().bind(userDashboardViewModel.getIsLoggedIn());
        loggedOutPane.visibleProperty().bind(userDashboardViewModel.getIsLoggedIn().not());

        loggedInPane.managedProperty().bind(loggedInPane.visibleProperty());
        loggedOutPane.managedProperty().bind(loggedOutPane.visibleProperty());

        myListButton.disableProperty().bind(userDashboardViewModel.getIsLoggedIn().not());
        overviewButton.disableProperty().bind(userDashboardViewModel.getIsLoggedIn().not());
    }

    @FXML
    public void handleNavButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        loadShowPane(clickedButton);
        setActiveButton(clickedButton);
    }

    private void setActiveButton(Button activeButton) {
        for (Button button : navButtons) {
            button.getStyleClass().remove("active");
        }
        activeButton.getStyleClass().add("active");
    }

    private void loadShowPane(Button button) {
        if (paneCache.containsKey(button)) {
            Node paneToShow = paneCache.get(button);
            System.out.println(button.getText() + " tab is loading");
            // ensure cached pane still fills the stack
            if (paneToShow instanceof Region) {
                Region r = (Region) paneToShow;
                r.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                if (!r.prefWidthProperty().isBound())
                    r.prefWidthProperty().bind(userStackPane.widthProperty());
                if (!r.prefHeightProperty().isBound())
                    r.prefHeightProperty().bind(userStackPane.heightProperty());
            } else {
                try {
                    AnchorPane.setTopAnchor(paneToShow, 0.0);
                    AnchorPane.setBottomAnchor(paneToShow, 0.0);
                    AnchorPane.setLeftAnchor(paneToShow, 0.0);
                    AnchorPane.setRightAnchor(paneToShow, 0.0);
                } catch (Exception ignored) {
                }
            }
            paneToShow.toFront();

            // call controller hook (if cached)
            Object cachedController = controllerCache.get(button);
            if (cachedController instanceof Refreshable) {
                ((Refreshable) cachedController).onFresh();
            }
        } else {
            String fxmlFile = getFxmlPathForButton(button).getFxmlFile();
            if (fxmlFile == null)
                return;

            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlFile)));
                Node newPane = loader.load();

                // cache controller returned by loader
                Object controller = loader.getController();
                if (controller instanceof SceneManaged) {
                    ((SceneManaged) controller).setSceneManager(this.sceneManager);
                }
                controllerCache.put(button, controller);

                newPane.setStyle("-fx-background-color: white;");
                newPane.setVisible(true);
                newPane.setManaged(true);

                if (newPane instanceof Region) {
                    Region r = (Region) newPane;
                    r.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    r.prefWidthProperty().bind(userStackPane.widthProperty());
                    r.prefHeightProperty().bind(userStackPane.heightProperty());
                } else {
                    try {
                        AnchorPane.setTopAnchor(newPane, 0.0);
                        AnchorPane.setBottomAnchor(newPane, 0.0);
                        AnchorPane.setLeftAnchor(newPane, 0.0);
                        AnchorPane.setRightAnchor(newPane, 0.0);
                    } catch (Exception ignored) {
                    }
                }

                // ensure hidden panes don't take space
                newPane.managedProperty().bind(newPane.visibleProperty());

                paneCache.put(button, newPane);
                userStackPane.getChildren().add(newPane);
                System.out.println(button.getText() + " tab is loading");
                newPane.toFront();

                // call onFresh on controller (not the Node)
                if (controller instanceof Refreshable) {
                    ((Refreshable) controller).onFresh();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private UserTabView getFxmlPathForButton(Button button) {
        if (button == discoverButton) {
            return UserTabView.USER_DISCOVER;
        } else if (button == myListButton) {
            return UserTabView.USER_MYLIST;
        } else if (button == overviewButton) {
            return UserTabView.USER_OVERVIEW;
        }
        return null;
    }

    @FXML
    public void handleLogout() {
        paneCache.clear();
        controllerCache.clear();
        userDashboardViewModel.logout();

        if (userDashboardViewModel.navigationRequestProperty().get() == null) {
            return;
        }
        if (userDashboardViewModel.navigationRequestProperty().get() == NavigationEvent.NAVIGATE_TO_USER_DASHBOARD) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION,
                    userDashboardBP.getScene().getWindow(),
                    "Logout Successful",
                    "Back to the User Dashboard");
            sceneManager.switchTo(View.USER_DASHBOARD);
        }

        // need navigate back to dashboard
        loadShowPane(discoverButton);
        setActiveButton(discoverButton);
    }

    @FXML
    public void handleLogin() {
        userDashboardViewModel.login();
        if (userDashboardViewModel.navigationRequestProperty().get() == null) {
            return;
        }
        if (userDashboardViewModel.navigationRequestProperty().get() == NavigationEvent.NAVIGATE_TO_LOGIN) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION,
                    userDashboardBP.getScene().getWindow(),
                    "Navigate to Login",
                    "Back to the Login Page");
            sceneManager.switchTo(View.LOGIN);
        }
    }

    @FXML
    public void handleRegister() {
        userDashboardViewModel.register();
        if (userDashboardViewModel.navigationRequestProperty().get() == null) {
            return;
        }
        if (userDashboardViewModel.navigationRequestProperty().get() == NavigationEvent.NAVIGATE_TO_REGISTER) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION,
                    userDashboardBP.getScene().getWindow(),
                    "Navigate to Register",
                    "Back to the Login Page");
            sceneManager.switchTo(View.REGISTER);
        }
    }

    @Override
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Override
    public void onFresh() {
        System.out.println("Fresh action: " + userDashboardViewModel.getIsLoggedIn().get() + " count: ");
    }

}
