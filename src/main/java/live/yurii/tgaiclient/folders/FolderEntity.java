package live.yurii.tgaiclient.folders;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import live.yurii.tgaiclient.chats.ChatEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "folders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderEntity {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private Integer id;

  @Column(name = "name", nullable = false)
  private String name;

  @ManyToMany
  @JoinTable(
      name = "chats_in_folder", // Name of the join table
      joinColumns = @JoinColumn(name = "folder_id"), // Column referencing ChatFolder
      inverseJoinColumns = @JoinColumn(name = "chat_id") // Column referencing Chat
  )
  @ToString.Exclude
  private List<ChatEntity> chats;

  public List<ChatEntity> getChats() {
    return chats == null ? Collections.emptyList() : Collections.unmodifiableList(chats);
  }

  public void putChat(@NotNull ChatEntity chat) {
    Objects.requireNonNull(chat, "Chat cannot be null");
    if (chats == null) {
      chats = Collections.emptyList();
    }
    if (chats.contains(chat)) {
      return;
    }
    chats.add(chat);
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    FolderEntity that = (FolderEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
  }
}
