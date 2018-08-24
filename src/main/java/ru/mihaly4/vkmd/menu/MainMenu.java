package ru.mihaly4.vkmd.menu;

import de.codecentric.centerdevice.MenuToolkit;
import javafx.scene.control.MenuBar;
import ru.mihaly4.vkmd.R;

public class MainMenu implements IMenu {
    @Override
    public void render() {
        MenuBar bar = new MenuBar();
        MenuToolkit tk = MenuToolkit.toolkit();
        bar.getMenus().add(tk.createDefaultApplicationMenu(R.TITLE_APP));
        tk.setGlobalMenuBar(bar);
    }
}
