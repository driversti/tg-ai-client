package live.yurii.tgaiclient.folders;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderItemRepository extends JpaRepository<FolderItemEntity, FolderItemEntity.FolderItemId> {
}
