package ru.mihaly4.vkmd.presenter;

public abstract class AbstractPresenter<V> {
    protected V view;

    public void bindView(V view) {
        this.view = view;
    }

    public void unbindView() {
        view = null;
    }
}
