package ru.mihaly4.ytpmusic.di;

import ru.mihaly4.ytpmusic.menu.MainMenu;
import ru.mihaly4.ytpmusic.view.MainView;
import dagger.Component;

@Component(modules = ApplicationModule.class)
@SharedScope
public interface ApplicationComponent {
    MainView makeMainView();
    MainMenu makeMainMenu();
}
