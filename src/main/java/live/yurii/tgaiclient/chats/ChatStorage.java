package live.yurii.tgaiclient.chats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ChatStorage {

  private final ChatJpaRepository repository;

  public List<ChatEntity> findAllById(Set<Long> ids) {
    return repository.findAllById(ids);
  }

  public void save(ChatEntity entity) {
    repository.save(entity);
  }

  public Optional<ChatEntity> findChat(long chatId) {
    return repository.findById(chatId);
  }
}
