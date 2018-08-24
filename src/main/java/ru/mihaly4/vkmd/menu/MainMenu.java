package ru.mihaly4.vkmd.menu;

import de.codecentric.centerdevice.MenuToolkit;
import javafx.scene.control.MenuBar;

public class MainMenu implements IMenu {
    @Override
    public void run() {
        MenuBar bar = new MenuBar();
        MenuToolkit tk = MenuToolkit.toolkit();
        bar.getMenus().add(tk.createDefaultApplicationMenu("VK Music Downloader"));
        tk.setGlobalMenuBar(bar);
    }
}
