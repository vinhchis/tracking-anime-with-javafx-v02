package com.project.navigation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class SceneManager{
    private final Stage stage;
    private final String basePath = "/com/project";
    private final Map<View, Parent> viewCache = new HashMap<>(); // View Cache

    public SceneManager(Stage stage) {
        this.stage = stage;
    }

    public void switchTo(View view) {
        try {
            Parent root;

            if(viewCache.containsKey(view)){
                System.out.println("Loading " + view + " from cache...");
                root = viewCache.get(view);
            }else{
                System.out.println("Loading " + view + " from FXML...");
                String fxmlPath = basePath + view.getFxmlFile();
                System.out.println("Loading " + fxmlPath + " from FXML...");

                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlPath)));
                root = loader.load();

                Object controller = loader.getController();
                if (controller instanceof SceneManaged) {
                    ((SceneManaged) controller).setSceneManager(this);
                }

                viewCache.put(view, root);
            }

            stage.getScene().setRoot(root);

        } catch (IOException e) {
            System.err.println("Can't not find FXML in View: " + view);
            e.printStackTrace();
        }
    }
}
/*
 * Controllers must implement SceneManaged interface to get SceneManager
 *
 */