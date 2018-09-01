package ru.mihaly4.vkmd.client;

public interface IVkClient {
    String fromAudio(int id, int offset);
    String fromWall(String id, int offset);
    Boolean login(String username, String password);
    int getUid();
}
