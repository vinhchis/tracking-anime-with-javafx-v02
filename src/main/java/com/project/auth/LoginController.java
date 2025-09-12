package com.project.auth;

import com.project.navigation.SceneManaged;
import com.project.navigation.SceneManager;
import com.project.navigation.View;
import com.project.util.AlertUtil;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;

public class LoginController implements SceneManaged {
    @FXML
    private BorderPane loginPane;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label messageLabel;

    private LoginViewModel viewModel;
    private SceneManager sceneManager;

    @FXML
    public void initialize() {
        // 1. Khởi tạo ViewModel
        viewModel = new LoginViewModel();

        // 2. Thực hiện data binding
        // Binding hai chiều: thay đổi trên UI sẽ cập nhật ViewModel và ngược lại
        usernameField.textProperty().bindBidirectional(viewModel.usernameProperty());
        passwordField.textProperty().bindBidirectional(viewModel.passwordProperty());

        // Binding một chiều: chỉ cập nhật UI từ ViewModel
        messageLabel.textProperty().bind(viewModel.messageProperty());
        loginButton.disableProperty().bind(viewModel.loginDisabledProperty());

        viewModel.navigationRequestProperty().addListener((obs, oldEvent, newEvent) -> {
            if (newEvent == null)
                return;
            Platform.runLater(() -> {
                Window owner = (loginPane.getScene() != null) ? loginPane.getScene().getWindow() : null;
                switch (newEvent) {
                    case NAVIGATE_TO_USER_DASHBOARD:
                        sceneManager.switchTo(View.USER_DASHBOARD);
                        AlertUtil.showAlert(Alert.AlertType.INFORMATION,
                                owner,
                                "Login Successful",
                                "Welcome to the User Dashboard!");
                        break;
                    case NAVIGATE_TO_ADMIN_DASHBOARD:
                        sceneManager.switchTo(View.ADMIN_DASHBOARD);
                        AlertUtil.showAlert(Alert.AlertType.INFORMATION,
                                owner,
                                "Login Successful",
                                "Welcome to the Admin Dashboard!");
                        break;
                }
                viewModel.navigationRequestProperty().set(null);
            });
        });
    }

    @FXML
    private void handleLogin() {
        viewModel.login();
        Window owner = (loginPane.getScene() != null) ? loginPane.getScene().getWindow() : null;
        if (viewModel.navigationRequestProperty().get() == null) {
            AlertUtil.showAlert(Alert.AlertType.ERROR,
                    owner,
                    "Login Failed",
                    viewModel.messageProperty().get());
            usernameField.requestFocus();
        }
    }

    @FXML
    private void goToRegister() {
        sceneManager.switchTo(View.REGISTER);

    }

    @Override
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

}
