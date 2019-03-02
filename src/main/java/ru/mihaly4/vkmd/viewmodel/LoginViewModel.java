package ru.mihaly4.vkmd.viewmodel;

import io.reactivex.subjects.PublishSubject;
import ru.mihaly4.vkmd.client.IVkClient;

import javax.annotation.Nonnull;

public class LoginViewModel {
    @Nonnull
    private IVkClient vkClient;
    @Nonnull
    private PublishSubject<String> status = PublishSubject.create();
    @Nonnull
    private PublishSubject<Boolean> isLogin = PublishSubject.create();

    public LoginViewModel(@Nonnull IVkClient vkClient) {
        this.vkClient = vkClient;
    }

    public void login(String username, String password) {
        new Thread(() -> {
            status.onNext("Wait...");

            if (vkClient.login(username, password)) {
                status.onNext("");
                isLogin.onNext(true);
            }

            status.onNext("Invalid credentials");

            isLogin.onNext(false);
        }).start();
    }


    @Nonnull
    public PublishSubject<String> getStatus() {
        return status;
    }

    @Nonnull
    public PublishSubject<Boolean> getIsLogin() {
        return isLogin;
    }
}
