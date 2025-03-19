package live.yurii.tgaiclient.common;

import org.drinkless.tdlib.TdApi;

import java.util.Optional;

public interface Storage {

  void putSuperGroup(TdApi.Supergroup supergroup);

  void putChat(TdApi.Chat chat);

  Optional<TdApi.Chat> findChat(long id);

  String chatTitle(long chatId);

  void putUser(TdApi.User user);

  Optional<TdApi.User> findUser(long userId);

  void updateUserStatus(long userId, TdApi.UserStatus status);

  void putBasicGroup(TdApi.BasicGroup basicGroup);

  void putSecretChat(TdApi.SecretChat secretChat);
}
