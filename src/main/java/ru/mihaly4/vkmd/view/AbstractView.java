package ru.mihaly4.vkmd.view;

import javafx.scene.Parent;
import javafx.stage.Stage;

public abstract class AbstractView {
    protected Stage stage;
    private Parent root;

    AbstractView(Stage stage) {
        this.stage = stage;
    }

    public void create() {
        root = onCreate();
    }

    public void start() {
        onStart(root);
    }

    protected abstract Parent onCreate();
    protected abstract void onStart(Parent root);
}
