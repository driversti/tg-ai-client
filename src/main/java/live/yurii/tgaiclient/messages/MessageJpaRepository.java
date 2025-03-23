package live.yurii.tgaiclient.messages;

import live.yurii.tgaiclient.common.SenderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface MessageJpaRepository extends JpaRepository<MessageEntity, Long> {

  Collection<MessageEntity> findBySender(SenderEntity sender);
}
