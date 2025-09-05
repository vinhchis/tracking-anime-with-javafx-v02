package com.project.user;

import com.project.navigation.SceneManaged;
import com.project.navigation.SceneManager;

public class UserDashboardViewModel implements SceneManaged {
    private SceneManager sceneManager;

    @Override
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

}
