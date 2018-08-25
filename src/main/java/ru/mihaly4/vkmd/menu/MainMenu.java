package ru.mihaly4.vkmd.menu;

import de.codecentric.centerdevice.MenuToolkit;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import ru.mihaly4.vkmd.R;
import ru.mihaly4.vkmd.view.AboutView;

public class MainMenu implements IMenu {
    private static final int ABOUT_INDEX = 0;

    private AboutView aboutView;

    public MainMenu(AboutView aboutView) {
        this.aboutView = aboutView;
    }

    @Override
    public void render() {
        MenuBar bar = new MenuBar();
        MenuToolkit tk = MenuToolkit.toolkit();

        Menu defaultApplicationMenu = tk.createDefaultApplicationMenu(R.APP_TITLE);
        defaultApplicationMenu.getItems().get(ABOUT_INDEX).setOnAction(value -> {
            aboutView.show();
        });

        bar.getMenus().add(defaultApplicationMenu);

        tk.setGlobalMenuBar(bar);
    }
}
