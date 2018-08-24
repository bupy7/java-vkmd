package ru.mihaly4.vkmd.view;

import javafx.stage.Stage;

public abstract class AbstractView implements Runnable {
    protected Stage stage;

    AbstractView(Stage stage) {
        this.stage = stage;
    }
}
