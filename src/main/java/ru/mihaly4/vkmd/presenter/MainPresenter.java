package ru.mihaly4.vkmd.presenter;

import ru.mihaly4.vkmd.client.IVkClient;
import ru.mihaly4.vkmd.model.Target;
import ru.mihaly4.vkmd.parser.TargetParser;
import ru.mihaly4.vkmd.repository.VkRepository;
import ru.mihaly4.vkmd.view.IMainView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MainPresenter extends AbstractPresenter<IMainView> {
    private IVkClient vkClient;
    private VkRepository vkRepository;

    public MainPresenter(IVkClient vkClient, VkRepository vkRepository) {
        this.vkClient = vkClient;
        this.vkRepository = vkRepository;
    }

    public Boolean isLogged() {
        return vkClient.getUid() != 0;
    }

    public CompletableFuture<Map<String, String[]>> parseAudioLinks(String url) {
        view.setStatus("Parsing...");

        Target target = TargetParser.parseTarget(url);

        if (target != null) {
            if (target.isAudioType()) {
                view.setStatus("Parsing from audio...");
                return vkRepository.findAllByAudio(Integer.valueOf(target.getValue())).thenApplyAsync(links -> {
                    view.setStatus("");

                    return links;
                });
            }
            if (target.isWallType()) {
                view.setStatus("Parsing from wall...");
                return vkRepository.findAllByWall(target.getValue()).thenApplyAsync(links -> {
                    view.setStatus("");

                    return links;
                });
            }
        }

        view.setStatus("Invalid URL");

        return CompletableFuture.supplyAsync(HashMap::new);
    }
}
