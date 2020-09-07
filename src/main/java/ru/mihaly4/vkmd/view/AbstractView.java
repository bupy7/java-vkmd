package ru.mihaly4.vkmd.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractView implements IView {
    @Nonnull
    protected Stage stage;
    @Nullable
    private Parent root;
    @Nonnull
    private List<IHiddenObserver> hiddenObservers = new ArrayList<>();

    AbstractView(@Nonnull Stage stage) {
        this.stage = stage;

        this.stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, (event) -> {
            event.consume();

            hide();
        });
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

        stage.setScene(null);
        root = null;

        onHidden();
    }

    public void subscribeOnHidden(@Nonnull IHiddenObserver observer) {
        hiddenObservers.add(observer);
    }

    public void unsubscribeOnHidden(@Nonnull IHiddenObserver observer) {
        hiddenObservers.remove(observer);
    }

    /**
     * Create own elements right here.
     * @return Parent
     */
    @Nonnull
    protected abstract Parent onCreate();

    /**
     * Initiate scene.
     */
    @Nonnull
    protected abstract Scene onStart(@Nonnull Parent root);
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
        root = onCreate();
    }

    private void start() {
        stage.setScene(onStart(root));
        stage.sizeToScene();
    }

    private void shown() {
        onShown(root);
    }

    private void notifyHiddenObservers() {
        hiddenObservers.forEach(IHiddenObserver::run);
    }

    interface IHiddenObserver {
        void run();
    }
}
