package ru.mihaly4.vkmd.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractView {
    protected Stage stage;
    @Nullable
    private Scene scene;
    private Parent root;
    private boolean created = false;

    AbstractView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        create();
        start();

        stage.show();

        resume();
    }

    protected abstract Parent onCreate();

    protected void create() {
        if (!created) {
            root = onCreate();

            created = true;
        }
    }

    protected void start() {
        if (scene == null) {
            onStart(root);

            scene = root.getScene();
        } else {
            stage.setScene(scene);
        }
    }

    protected void resume() {
        onResume(root);
    }

    protected void onStart(Parent root) {
        // nothing
    }

    protected void onResume(Parent root) {
        // nothing
    }
}
