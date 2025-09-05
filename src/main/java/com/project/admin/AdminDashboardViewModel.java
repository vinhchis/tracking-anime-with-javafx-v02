package com.project.admin;

import com.project.navigation.SceneManaged;
import com.project.navigation.SceneManager;

public class AdminDashboardViewModel implements SceneManaged {
    private  SceneManager sceneManager;
    @Override
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

}
