package com.project;

import com.project.navigation.SceneManager;
import com.project.navigation.View;
import com.project.util.JpaUtil;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(new VBox(), 600, 400));
        primaryStage.setTitle("Scene Manager Demo");

        // 1. Tạo một instance duy nhất của SceneManager
        SceneManager sceneManager = new SceneManager(primaryStage); // not change

        // 2. Sử dụng SceneManager để hiển thị màn hình đầu tiên
        sceneManager.switchTo(View.REGISTER);

        primaryStage.show();
    }

    @Override
    public void stop(){
        JpaUtil.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
