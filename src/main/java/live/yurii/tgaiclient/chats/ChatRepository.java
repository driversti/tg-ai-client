package live.yurii.tgaiclient.chats;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

  List<ChatEntity> findAllByIdIn(List<Long> ids);
}
