package ru.mihaly4.vkmd.client;

import ru.mihaly4.vkmd.model.Credential;

import java.util.concurrent.CompletableFuture;

public interface IVkClient {
    String fromAudio(int id, int offset);
    String fromWall(String id, int offset);
    CompletableFuture<Credential> login(String username, String password);
}
