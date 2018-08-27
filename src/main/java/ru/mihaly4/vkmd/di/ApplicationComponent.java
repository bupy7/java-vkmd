package ru.mihaly4.vkmd.di;

import ru.mihaly4.vkmd.menu.MainMenu;
import ru.mihaly4.vkmd.view.MainView;
import dagger.Component;

import javax.inject.Singleton;

@Component(modules = ApplicationModule.class)
@Singleton
public interface ApplicationComponent {
    MainView makeMainView();
    MainMenu makeMainMenu();
}
