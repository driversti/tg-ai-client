package live.yurii.tgaiclient.chats;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatJpaRepository extends JpaRepository<ChatEntity, Long> {

  List<ChatEntity> findAllByInFolders_Id(long folderId);
}
