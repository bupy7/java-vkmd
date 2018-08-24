package ru.mihaly4.vkmd.di;

import ru.mihaly4.vkmd.menu.MainMenu;
import ru.mihaly4.vkmd.view.MainView;
import dagger.Component;

@Component(modules = ApplicationModule.class)
@SharedScope
public interface ApplicationComponent {
    MainView makeMainView();
    MainMenu makeMainMenu();
}
