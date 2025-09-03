package com.project;

import com.project.navigation.SceneManager;
import com.project.navigation.View;
import com.project.repository.AccountRepository;
import com.project.repository.SeasonRepository;
import com.project.repository.StudioRepository;
import com.project.util.JpaUtil;
import com.project.util.SeedData;

import jakarta.persistence.EntityManagerFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // SEED DATA //
        EntityManagerFactory emf = JpaUtil.getEntityManagerFactory();

        AccountRepository accountRepository = new AccountRepository(emf);
        SeasonRepository seasonRepository = new SeasonRepository(emf);
        StudioRepository studioRepository = new StudioRepository(emf);

        SeedData seeder = new SeedData(accountRepository, seasonRepository, studioRepository);
        seeder.seeds();

        // ---------------- //
        primaryStage.setScene(new Scene(new VBox(), 600, 400));
        primaryStage.setTitle("Tracking Anime Application");

        // 1. Tạo một instance duy nhất của SceneManager
        SceneManager sceneManager = new SceneManager(primaryStage); // not change

        // 2. Sử dụng SceneManager để hiển thị màn hình đầu tiên
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
