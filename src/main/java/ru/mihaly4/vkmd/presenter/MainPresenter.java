package ru.mihaly4.vkmd.presenter;

import ru.mihaly4.vkmd.client.IVkClient;
import ru.mihaly4.vkmd.view.IMainView;

public class MainPresenter extends AbstractPresenter<IMainView> {
    private IVkClient vkClient;

    public MainPresenter(IVkClient vkClient) {
        this.vkClient = vkClient;
    }

    public Boolean isLogged() {
        return vkClient.getUid() != 0;
    }
}
