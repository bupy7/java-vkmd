package ru.mihaly4.vkmd.menu;

import de.codecentric.centerdevice.MenuToolkit;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import org.apache.commons.lang3.SystemUtils;
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
        // deprecated 2.0.0
        if (!SystemUtils.IS_OS_MAC) {
            return;
        }

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
