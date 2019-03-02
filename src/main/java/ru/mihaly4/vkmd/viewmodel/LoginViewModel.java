package ru.mihaly4.vkmd.viewmodel;

import io.reactivex.subjects.BehaviorSubject;
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
    @Nonnull
    private BehaviorSubject<Boolean> isProcessing = BehaviorSubject.createDefault(false);

    public LoginViewModel(@Nonnull IVkClient vkClient) {
        this.vkClient = vkClient;
    }

    public void login(@Nonnull String username, @Nonnull String password) {
        new Thread(() -> {
            status.onNext("Wait...");
            isProcessing.onNext(true);

            if (vkClient.login(username, password)) {
                status.onNext("");
                isLogin.onNext(true);
                isProcessing.onNext(false);
            } else {
                status.onNext("Invalid credentials");
                isLogin.onNext(false);
                isProcessing.onNext(false);
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
}
