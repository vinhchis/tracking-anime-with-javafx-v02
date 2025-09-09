package com.project.auth;

import com.project.navigation.NavigationEvent;
import com.project.navigation.SceneManaged;
import com.project.navigation.SceneManager;
import com.project.navigation.View;
import com.project.util.AlertUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RegisterController implements SceneManaged {
    @FXML
    TextField usernameField;

    @FXML
    TextField passwordField;

    @FXML
    TextField confirmPasswordField;

    @FXML
    Label messageLabel;

    @FXML
    Button registerButton;

    @FXML
    Button backToLoginButton;


    private SceneManager sceneManager;
    private RegisterViewModel registerViewModel;

    @Override
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize(){
        registerViewModel = new RegisterViewModel();

        // bind
        usernameField.textProperty().bindBidirectional(registerViewModel.usernameProperty());
        passwordField.textProperty().bindBidirectional(registerViewModel.passwordProperty());
        confirmPasswordField.textProperty().bindBidirectional(registerViewModel.confirmPasswordProperty());
        messageLabel.textProperty().bind(registerViewModel.messageProperty());
        registerButton.disableProperty().bind(registerViewModel.registerDisabledProperty());
    }


    public void backToLogin(){
        sceneManager.switchTo(View.LOGIN);
    }

    public void register(){
        // validate and register
        registerViewModel.register();
        if (registerViewModel.navigationRequestProperty().get() == null) {
            return;
        }
        if (registerViewModel.navigationRequestProperty().get() == NavigationEvent.NAVIGATE_TO_LOGIN) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION,
                                usernameField.getScene().getWindow(),
                                "Register Successful",
                                "Back to the Login Page");
            sceneManager.switchTo(View.LOGIN);
        }
    }

}
