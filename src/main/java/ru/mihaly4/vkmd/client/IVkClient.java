package ru.mihaly4.vkmd.client;

public interface IVkClient {
    String fromProfile(int id, int offset);
    String fromCommunity(String id, int offset);
    String getRemixSid();
    int getUid();
}
