package live.yurii.tgaiclient.chats;

import live.yurii.tgaiclient.common.SenderEntity;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
public class ChatMapper {
  // TODO: consider using MapStruct

  public ChatDTO toDTO(ChatEntity entity) {
    return ChatDTO.builder()
        .id(entity.getId())
        .title(entity.getTitle())
        .build();
  }

  public Collection<ChatDTO> toDTO(Collection<ChatEntity> entities) {
    return entities.stream()
        .map(this::toDTO)
        .toList();
  }

  public ChatEntity toEntity(TdApi.Chat chat) {
    return ChatEntity.create(chat.id, chat.title)
        .type(resolveChatType(chat.type))
        .lastReadInboxMessageId(chat.lastReadInboxMessageId)
        .lastReadOutboxMessageId(chat.lastReadOutboxMessageId)
        .messageAutoDeleteTime(chat.messageAutoDeleteTime);
  }

  private ChatEntity.ChatType resolveChatType(TdApi.ChatType type) {
    return switch (type.getConstructor()) {
      case TdApi.ChatTypeSupergroup.CONSTRUCTOR -> ChatEntity.ChatType.SUPER_GROUP;
      case TdApi.ChatTypeSecret.CONSTRUCTOR -> ChatEntity.ChatType.SECRET;
      case TdApi.ChatTypePrivate.CONSTRUCTOR -> ChatEntity.ChatType.PRIVATE;
      default -> ChatEntity.ChatType.BASIC_GROUP;
    };
  }
}
