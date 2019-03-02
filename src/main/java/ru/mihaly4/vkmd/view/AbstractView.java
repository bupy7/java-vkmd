package ru.mihaly4.vkmd.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractView implements IView {
    protected Stage stage;
    @Nullable
    private Scene scene;
    private Parent root;
    private boolean created = false;
    private List<IHiddenObserver> hiddenObservers = new ArrayList<>();

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
        stage.hide();
        onHidden();
    }

    public void subscribeOnHidden(IHiddenObserver observer) {
        hiddenObservers.add(observer);
    }

    public void unsubscribeOnHidden(IHiddenObserver observer) {
        hiddenObservers.remove(observer);
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
        notifyHiddenObservers();
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

    private void notifyHiddenObservers() {
        hiddenObservers.forEach(observer -> observer.run());
    }

    interface IHiddenObserver {
        void run();
    }
}
