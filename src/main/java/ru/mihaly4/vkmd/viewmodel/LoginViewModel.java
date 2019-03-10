package ru.mihaly4.vkmd.viewmodel;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import ru.mihaly4.vkmd.client.IVkClient;
import ru.mihaly4.vkmd.model.LoginResponse;

import javax.annotation.Nonnull;

public class LoginViewModel {
    @Nonnull
    private IVkClient vkClient;
    @Nonnull
    private PublishSubject<String> status = PublishSubject.create();
    @Nonnull
    private PublishSubject<Boolean> isLogin = PublishSubject.create();
    @Nonnull
    private BehaviorSubject<Boolean> isProcessing = BehaviorSubject.createDefault(false);
    @Nonnull
    private PublishSubject<String> captchaUrl = PublishSubject.create();
    @Nonnull
    private BehaviorSubject<String> username = BehaviorSubject.createDefault("");
    @Nonnull
    private BehaviorSubject<String> password = BehaviorSubject.createDefault("");
    @Nonnull
    private BehaviorSubject<String> captchaCode = BehaviorSubject.createDefault("");

    public LoginViewModel(@Nonnull IVkClient vkClient) {
        this.vkClient = vkClient;
    }

    public void login() {
        new Thread(() -> {
            status.onNext("Wait...");
            isProcessing.onNext(true);

            LoginResponse result = vkClient.login(username.getValue(), password.getValue(), captchaCode.getValue());

            if (result.isLoggedIn()) {
                status.onNext("");
                isLogin.onNext(true);
                isProcessing.onNext(false);
            } else {
                status.onNext("Invalid credentials");
                isLogin.onNext(false);
                isProcessing.onNext(false);
                this.captchaUrl.onNext(result.getCaptcha());
            }
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

    @Nonnull
    public BehaviorSubject<Boolean> getIsProcessing() {
        return isProcessing;
    }

    @Nonnull
    public PublishSubject<String> getCaptchaUrl() {
        return captchaUrl;
    }

    @Nonnull
    public BehaviorSubject<String> getUsername() {
        return username;
    }

    @Nonnull
    public BehaviorSubject<String> getPassword() {
        return password;
    }

    @Nonnull
    public BehaviorSubject<String> getCaptchaCode() {
        return captchaCode;
    }
}
