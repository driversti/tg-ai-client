package live.yurii.tgaiclient.chats;

import live.yurii.tgaiclient.common.InMemoryStorage;
import live.yurii.tgaiclient.messages.UpdateUnreadMessageCountEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatHandler {

  private final InMemoryStorage storage;

  @EventListener
  public void onUpdateNewChatEvent(UpdateNewChatEvent event) {
    TdApi.UpdateNewChat update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateNewChat.CONSTRUCTOR) {
      return;
    }
    log.info("Chat \"{}\" has new updates", update.chat.title);
    storage.putChat(update.chat);
  }

  @EventListener
  public void onUpdateChatAddedToListEvent(UpdateChatAddedToListEvent event) {
    TdApi.UpdateChatAddedToList update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateChatAddedToList.CONSTRUCTOR) {
      return;
    }
    switch (update.chatList.getConstructor()) {
      case TdApi.ChatListMain.CONSTRUCTOR -> handleAddedToChatMainList(update);
      case TdApi.ChatListArchive.CONSTRUCTOR -> handleAddedToChatArchiveList(update);
      case TdApi.ChatListFolder.CONSTRUCTOR -> handleAddedToChatFolderList(update);
      default -> log.warn("Not implemented chat added to list: {}", update.chatList.getConstructor());
    }
  }

  @EventListener
  public void onUpdateChatReadInboxEvent(UpdateChatReadInboxEvent event) {
    TdApi.UpdateChatReadInbox update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateChatReadInbox.CONSTRUCTOR) {
      return;
    }
    log.trace("Chat {}, last read message: {}, unread count: {}",
        update.chatId, update.lastReadInboxMessageId, update.unreadCount);
  }

  @EventListener
  public void onUpdateUnreadMessageCountEvent(UpdateUnreadMessageCountEvent event) {
    TdApi.UpdateUnreadMessageCount update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateUnreadMessageCount.CONSTRUCTOR) {
      return;
    }
    log.trace("Chat list {} has {} unread messages, and {} unread unmuted messages",
        update.chatList.getConstructor(), update.unreadCount, update.unreadUnmutedCount);
  }

  @EventListener
  public void onUpdateSecretChatEvent(UpdateSecretChatEvent event) {
    TdApi.UpdateSecretChat update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateSecretChat.CONSTRUCTOR) {
      return;
    }
    storage.putSecretChat(update.secretChat);
  }

  @EventListener
  public void onUpdateChatTitleEvent(UpdateChatTitleEvent event) {
    TdApi.UpdateChatTitle update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateChatTitle.CONSTRUCTOR) {
      return;
    }
    storage.findChat(update.chatId)
        .ifPresent(chat -> {
          log.info("Chat {} ({}) renamed to {}", chat.title, chat.id, update.title);
          chat.title = update.title;
        });
  }

  private void handleAddedToChatMainList(TdApi.UpdateChatAddedToList update) {
    log.trace("Chat {} added to main list", update.chatId);
  }

  private void handleAddedToChatArchiveList(TdApi.UpdateChatAddedToList update) {
    log.trace("Chat {} added to archive list", update.chatId);
  }

  private void handleAddedToChatFolderList(TdApi.UpdateChatAddedToList update) {
    log.trace("Chat {} added to folder list", update.chatId);
  }
}
