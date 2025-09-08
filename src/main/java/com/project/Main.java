package com.project;

import com.project.navigation.SceneManager;
import com.project.navigation.View;
import com.project.util.JpaUtil;
import com.project.util.SeedData;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // SEED DATA //
        SeedData.seeds();

        // ---------------- //
        primaryStage.setScene(new Scene(new VBox()));
        primaryStage.setTitle("Tracking Anime Application");

        // inject Stage to SceneManager
        SceneManager sceneManager = new SceneManager(primaryStage); // not change

        sceneManager.switchTo(View.LOGIN);

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
