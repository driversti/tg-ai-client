package live.yurii.tgaiclient.folders;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "folders")
public class FolderEntity {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private Integer id;

  @Column(name = "name", nullable = false)
  private String name;

  @OneToMany(
      mappedBy = "folder",
      cascade = CascadeType.ALL, // Cascade persist, merge, remove etc. to FolderItems
      orphanRemoval = true, // Delete FolderItems removed from this collection
      fetch = FetchType.LAZY
  )
  private Set<FolderItemEntity> folderItems = new HashSet<>();

  public FolderEntity(Integer id, String name) {
    this.id = id;
    this.name = name;
  }

  public void addItem(FolderItemEntity folderItem) {
    folderItems.add(folderItem);
    folderItem.setFolder(this);
  }

  public void removeItem(FolderItemEntity folderItem) {
    folderItems.remove(folderItem);
    folderItem.setFolder(null);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof FolderEntity that)) return false;
    return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName());
  }
}
