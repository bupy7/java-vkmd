package ru.mihaly4.vkmd.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractView implements IView {
    protected Stage stage;
    @Nullable
    private Scene scene;
    private Parent root;
    private boolean created = false;

    AbstractView(Stage stage) {
        this.stage = stage;

        this.stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, (event) -> onHidden());
    }

    @Override
    public void show() {
        create();
        start();

        stage.show();

        shown();
    }

    @Override
    public void hide() {
        stage.close();
    }

    /**
     * Create own elements right here.
     * @return Parent
     */
    protected abstract Parent onCreate();

    /**
     * Initiate scene.
     */
    protected void onStart(Parent root) {
        // nothing
    }

    /**
     * Here you can set focus on any element or something else.
     */
    protected void onShown(Parent root) {
        // nothing
    }

    /**
     * Closing window.
     */
    protected void onHidden() {
        // nothing
    }

    private void create() {
        if (!created) {
            root = onCreate();

            created = true;
        }
    }

    private void start() {
        if (scene == null) {
            onStart(root);

            scene = root.getScene();
        } else {
            stage.setScene(scene);
        }
    }

    private void shown() {
        onShown(root);
    }
}
