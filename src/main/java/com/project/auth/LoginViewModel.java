package com.project.auth;

import com.project.service.AuthService;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoginViewModel   {
     // Properties để binding với các control trên View
    private final StringProperty username = new SimpleStringProperty("");
    private final StringProperty password = new SimpleStringProperty("");
    private final StringProperty message = new SimpleStringProperty("");
    private final BooleanProperty loginDisabled = new SimpleBooleanProperty(true);

    private final AuthService authService = new AuthService();

    public LoginViewModel() {
        // Vô hiệu hóa nút login nếu username hoặc password rỗng
        loginDisabled.bind(Bindings.createBooleanBinding(
            () -> username.get().trim().isEmpty() || password.get().trim().isEmpty(),
            username,
            password
        ));
    }

    public void login() {
        boolean isAuthenticated = authService.authenticate(username.get(), password.get());

        if (isAuthenticated) {
            message.set("Đăng nhập thành công! Chào mừng " + username.get());
            // Thêm logic chuyển trang ở đây, ví dụ: sceneManager.switchTo(View.DASHBOARD);
        } else {
            message.set("Lỗi: Tên đăng nhập hoặc mật khẩu không đúng.");
        }
    }

    // Cung cấp các getters cho Properties để View có thể bind
    public StringProperty usernameProperty() { return username; }
    public StringProperty passwordProperty() { return password; }
    public StringProperty messageProperty() { return message; }
    public BooleanProperty loginDisabledProperty() { return loginDisabled; }


}
