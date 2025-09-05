package com.project.navigation;

public enum View {
    // Auth Module
    LOGIN("/auth/LoginView.fxml"),
    REGISTER("/auth/RegisterView.fxml"),

    // Admin Module
    ADMIN_DASHBOARD("/admin/AdminDashboardView.fxml"),
    USER_MANAGEMENT("/admin/UserManagementView.fxml"),

    // User Module
    USER_DASHBOARD("/user/UserDashboardView.fxml"),
    USER_PROFILE("/user/ProfileView.fxml");

    private final String fxmlFile;

    View(String fxmlFile) {
        this.fxmlFile = fxmlFile;
    }

    public String getFxmlFile() {
        return fxmlFile;
    }
}
