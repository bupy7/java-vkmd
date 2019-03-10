package ru.mihaly4.vkmd.client;

import ru.mihaly4.vkmd.model.LoginResponse;

import javax.annotation.Nonnull;

public interface IVkClient {
    @Nonnull
    String fromAudio(int id, int offset);
    @Nonnull
    String fromWall(@Nonnull String id, int offset);
    @Nonnull
    LoginResponse login(@Nonnull String username, @Nonnull String password, @Nonnull String captchaCode);
    int getUid();
}
