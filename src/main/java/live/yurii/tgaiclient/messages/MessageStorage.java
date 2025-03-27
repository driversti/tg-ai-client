package live.yurii.tgaiclient.messages;

import live.yurii.tgaiclient.common.SenderEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MessageStorage {

  private final MessageJpaRepository repository;

  @Transactional
  public void save(MessageEntity message) {
    repository.findById(message.getId())
        .ifPresentOrElse(updateContent(message), saveNewMessage(message));
  }

  private Runnable saveNewMessage(MessageEntity message) {
    return () -> repository.save(message);
  }

  public Collection<MessageEntity> findAll() {
    return repository.findAll();
  }

  public Collection<MessageEntity> findBySender(SenderEntity senderId) {
    return repository.findBySender(senderId);
  }

  public void deleteById(long id) {
    repository.deleteById(id);
  }

  public Optional<MessageEntity> findBySender(long chatId) {
    return repository.findBySender_Id(chatId);
  }

  public Optional<MessageEntity> findById(long messageId) {
    return repository.findById(messageId);
  }

  private static Consumer<MessageEntity> updateContent(MessageEntity message) {
    return existingMessage -> {
      existingMessage.setContentText(message.getContentText());
      log.trace("Updated message: {}", existingMessage.getId());
    };
  }
}
