package live.yurii.tgaiclient.folders;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "folder_items")
public class FolderItemEntity {

  @EmbeddedId
  private FolderItemId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("folderId")
  @JoinColumn(name = "folder_id", insertable = false, updatable = false)
  private FolderEntity folder;

  public FolderItemEntity(Long itemId, FolderEntity folder) {
    this.id = new FolderItemId(folder.getId(), itemId);
    this.folder = folder;
  }

  public FolderItemEntity(FolderItemId id) {
    this.id = id;
  }

  public Long getItemId() {
    return id != null ? id.getItemId() : null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FolderItemEntity that = (FolderItemEntity) o;
    // Check if both IDs are null or if they are equal
    return (id == null && that.id == null) || (id != null && id.equals(that.id));
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : System.identityHashCode(this);
  }

  @Setter
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode
  @Embeddable
  public static class FolderItemId implements Serializable {

    @Column(name = "folder_id", nullable = false)
    private Integer folderId;

    @Column(name = "item_id", nullable = false)
    private Long itemId; // ID of the chat (-) or user (+)
  }
}
