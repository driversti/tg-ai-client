package live.yurii.tgaiclient.chats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatEventListener {

  private final ChatRepository chatRepository;
  private final ChatMapper chatMapper;

  @Transactional
  @EventListener
  public void onUpdateNewChatEvent(UpdateNewChatEvent event) {
    TdApi.UpdateNewChat update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateNewChat.CONSTRUCTOR) {
      return;
    }

    chatRepository.findById(update.chat.id)
        .ifPresentOrElse(
            chatEntity -> updateChat(chatEntity, update),
            () -> addChat(update)
        );
  }

  @Transactional
  @EventListener
  public void onUpdateBasicGroupEvent(UpdateBasicGroupEvent event) {
    TdApi.UpdateBasicGroup update = event.getUpdate();
    if (update == null || update.getConstructor() != TdApi.UpdateBasicGroup.CONSTRUCTOR) {
      return;
    }
    // I don't know yet how this update can be used
  }

  private void updateChat(ChatEntity chatEntity, TdApi.UpdateNewChat update) {
    chatMapper.updateEntity(chatEntity, update.chat);
    log.trace("Updated chat {}", chatEntity.getTitle());
  }

  private void addChat(TdApi.UpdateNewChat update) {
    ChatEntity entity = chatMapper.toEntity(update.chat);
    chatRepository.save(entity);
    log.debug("Added new chat {}", entity.getTitle());
  }
}
