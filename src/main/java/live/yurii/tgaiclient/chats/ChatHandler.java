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
    log.trace("Chat \"{}\" has new updates", update.chat.title);
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

  @EventListener
  public void onUpdateChatActiveStoriesEvent(UpdateChatActiveStoriesEvent event) {
    TdApi.UpdateChatActiveStories update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateChatActiveStories.CONSTRUCTOR) {
      return;
    }
    TdApi.ChatActiveStories activeStories = update.activeStories;
    log.trace("Chat {} has {} active stories", activeStories.chatId, activeStories.stories.length);
  }

  @EventListener
  public void onUpdateChatActionEvent(UpdateChatActionEvent event) {
    TdApi.UpdateChatAction update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateChatAction.CONSTRUCTOR) {
      return;
    }
    log.trace("Chat {} has new action: {}", update.chatId, update.action);
  }

  @EventListener
  public void onUpdateChatNotificationSettingsEvent(UpdateChatNotificationSettingsEvent event) {
    TdApi.UpdateChatNotificationSettings update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateChatNotificationSettings.CONSTRUCTOR) {
      return;
    }
    String chatTitle = storage.findChat(update.chatId).map(c -> c.title).orElse("");
    log.trace("Chat {} ({}) has new notification settings", chatTitle, update.chatId);
  }

  @EventListener
  public void onUpdateChatPositionEvent(UpdateChatPositionEvent event) {
    // ignore for now
  }

  @EventListener
  public void onUpdateChatReadOutboxEvent(UpdateChatReadOutboxEvent event) {
    // ignore for now
  }

  @EventListener
  public void onUpdateUnreadChatCountEvent(UpdateUnreadChatCountEvent event) {
    // ignore for now
  }

  @EventListener
  public void onUpdateChatRemovedFromListEvent(UpdateChatRemovedFromListEvent event) {
    // ignore for now
  }

  @EventListener
  public void onUpdateHavePendingNotificationsEvent(UpdateHavePendingNotificationsEvent event) {
    // ignore for now
  }

  @EventListener
  public void onUpdateChatAvailableReactionsEvent(UpdateChatAvailableReactionsEvent event) {
    // ignore for now
  }

  @EventListener
  public void onUpdateChatIsTranslatableEvent(UpdateChatIsTranslatableEvent event) {
    // ignore for now
  }

  @EventListener
  public void onUpdateChatMessageSenderEvent(UpdateChatMessageSenderEvent event) {
    // ignore for now
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
