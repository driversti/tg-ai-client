package live.yurii.tgaiclient.messages;

import live.yurii.tgaiclient.common.SenderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface MessageJpaRepository extends JpaRepository<MessageEntity, Long> {

  Collection<MessageEntity> findBySender(SenderEntity sender);

  Optional<MessageEntity> findBySender_Id(long chatId);
}
