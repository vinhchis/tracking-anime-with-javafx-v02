package com.project.navigation;

public enum TabView {
    // tabs of Admin Page
    ADMIN_HOME("/admin/HomeView.fxml"),
    ADMIN_ACCOUNT("/admin/AccountView.fxml"),
    ADMIN_ANIME("/admin/AnimeView.fxml"),
    ADMIN_SEASON("/admin/SeasonView.fxml"),
    ADMIN_STUDIO("/admin/StudioView.fxml"),
    ADMIN_EPISODE("/admin/EpisodeView.fxml");




    private final String fxmlFile;

    TabView(String fxmlFile) {
        this.fxmlFile = "/com/project" + fxmlFile;
    }

    public String getFxmlFile() {
        return fxmlFile;
    }
}

