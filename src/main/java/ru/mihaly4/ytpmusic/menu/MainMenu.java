package ru.mihaly4.ytpmusic.menu;

import de.codecentric.centerdevice.MenuToolkit;
import javafx.scene.control.MenuBar;

public class MainMenu implements Runnable {
    @Override
    public void run() {
        MenuBar bar = new MenuBar();
        MenuToolkit tk = MenuToolkit.toolkit();
        bar.getMenus().add(tk.createDefaultApplicationMenu("YTP Music"));
        tk.setGlobalMenuBar(bar);
    }
}
