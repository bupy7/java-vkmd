package ru.mihaly4.ytpmusic.view;

import javafx.stage.Stage;

public abstract class AbstractView {
    protected Stage stage;

    AbstractView(Stage stage) {
        this.stage = stage;
    }

    abstract protected void render();
}
