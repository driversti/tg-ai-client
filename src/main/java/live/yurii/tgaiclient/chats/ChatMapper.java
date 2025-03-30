package live.yurii.tgaiclient.chats;

import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChatMapper {

  public ChatEntity toEntity(TdApi.Chat chat) {
    ChatEntity entity = new ChatEntity();
    entity.setId(chat.id);
    entity.setTitle(chat.title);
    entity.setType(getChatType(chat.type));
    entity.setLastReadInboxMessageId(chat.lastReadInboxMessageId);
    entity.setLastReadOutboxMessageId(chat.lastReadOutboxMessageId);
    entity.setMessageAutoDeleteTime(chat.messageAutoDeleteTime);
    return entity;
  }

  public void updateEntity(ChatEntity entity, TdApi.Chat chat) {
    entity.setTitle(chat.title);
    entity.setType(getChatType(chat.type));
    entity.setLastReadInboxMessageId(chat.lastReadInboxMessageId);
    entity.setLastReadOutboxMessageId(chat.lastReadOutboxMessageId);
    entity.setMessageAutoDeleteTime(chat.messageAutoDeleteTime);
  }

  private ChatEntity.ChatType getChatType(TdApi.ChatType type) {
    return switch (type.getConstructor()) {
      case TdApi.ChatTypePrivate.CONSTRUCTOR -> ChatEntity.ChatType.PRIVATE;
      case TdApi.ChatTypeSecret.CONSTRUCTOR -> ChatEntity.ChatType.SECRET;
      case TdApi.ChatTypeBasicGroup.CONSTRUCTOR -> ChatEntity.ChatType.BASIC_GROUP;
      case TdApi.ChatTypeSupergroup.CONSTRUCTOR -> ChatEntity.ChatType.SUPERGROUP;
      default -> throw new IllegalArgumentException("Unknown chat type: " + type);
    };
  }
}
