package live.yurii.tgaiclient.messages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageEventListener {

  private final MessageRepository messageRepository;
  private final MessageMapper messageMapper;
  private final Map<Long, String> ignoredChats;

  @Transactional
  @EventListener
  public void onUpdateNewMessageEvent(UpdateNewMessageEvent event) {
    TdApi.UpdateNewMessage update = event.getUpdate();
    if (update == null ||
        update.getConstructor() != TdApi.UpdateNewMessage.CONSTRUCTOR ||
        ignoredChats.containsKey(messageMapper.getSenderId(update.message.senderId)) ||
        ignoredChats.containsKey(update.message.chatId) ||
        isBlank(messageMapper.getText(update.message.content))
    ) {
      return;
    }

    saveOrUpdate(update.message);
  }

  @Transactional
  @EventListener
  public void onUpdateMessageContentEvent(UpdateMessageContentEvent event) {
    TdApi.UpdateMessageContent update = event.getUpdate();
    if (update == null ||
        update.getConstructor() != TdApi.UpdateMessageContent.CONSTRUCTOR ||
        ignoredChats.containsKey(update.chatId)
    ) {
      return;
    }

    messageRepository.findById(update.messageId)
        .ifPresent(entity -> entity.setText(messageMapper.getText(update.newContent)));
  }

  @Transactional
  @EventListener
  public void onUpdateMessageEditedEvent(UpdateMessageEditedEvent event) {
    TdApi.UpdateMessageEdited update = event.getUpdate();
    if (update == null ||
        update.getConstructor() != TdApi.UpdateMessageEdited.CONSTRUCTOR ||
        ignoredChats.containsKey(update.messageId) ||
        ignoredChats.containsKey(update.chatId)
    ) {
      return;
    }

    messageRepository.findById(update.messageId)
        .ifPresent(entity -> entity.setEditDate(Instant.ofEpochSecond(update.editDate)));
  }

  private void saveOrUpdate(TdApi.Message message) {
    messageRepository.findById(message.id)
        .ifPresentOrElse(
            existingMessage -> messageMapper.updateEntity(existingMessage, message),
            () -> messageRepository.save(messageMapper.toEntity(message))
        );
  }
}
