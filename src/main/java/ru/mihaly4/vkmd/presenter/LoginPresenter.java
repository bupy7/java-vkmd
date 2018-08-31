package ru.mihaly4.vkmd.presenter;

import ru.mihaly4.vkmd.client.IVkClient;
import ru.mihaly4.vkmd.view.ILoginView;

import java.util.concurrent.CompletableFuture;

public class LoginPresenter extends AbstractPresenter<ILoginView> {
    private IVkClient vkClient;

    public LoginPresenter(IVkClient vkClient) {
        this.vkClient = vkClient;
    }

    public CompletableFuture<Boolean> login(String username, String password) {
        return CompletableFuture.supplyAsync(() -> {
            view.setStatus("Wait...");

            if (vkClient.login(username, password)) {
                view.setStatus("");

                return true;
            }

            view.setStatus("Invalid credentials");

            return false;
        });
    }
}
