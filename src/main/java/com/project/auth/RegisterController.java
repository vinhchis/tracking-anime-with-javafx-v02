package com.project.auth;

import com.project.navigation.SceneManaged;
import com.project.navigation.SceneManager;

public class RegisterController implements SceneManaged {

    private SceneManager sceneManager;
    @Override
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

}
