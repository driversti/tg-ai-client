package live.yurii.tgaiclient.chats;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import live.yurii.tgaiclient.common.SenderEntity;
import live.yurii.tgaiclient.folders.FolderEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "chats")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatEntity extends SenderEntity {

  @Column(name = "title", nullable = false, updatable = false)
  private String title;

  @Enumerated(EnumType.STRING)
  @Column(name = "chat_type", nullable = false, updatable = false)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  private ChatType type; // We store the type as a String, with a DB constraint.

  @ManyToMany(mappedBy = "chats")
  private List<FolderEntity> inFolders;

  @Column(name = "last_read_inbox_message_id")
  private Long lastReadInboxMessageId;

  @Column(name = "last_read_outbox_message_id")
  private Long lastReadOutboxMessageId;

  @Column(name = "message_auto_delete_time")
  private Integer messageAutoDeleteTime;

  // TODO: add 'int unreadCount' field

  public ChatEntity(Long id, String title) {
    super(id, SenderType.CHAT);
    this.title = title;
  }

  public static ChatEntity create(Long id, String title) {
    return new ChatEntity(id, title);
  }

  public ChatEntity title(String title) {
    this.title = title;
    return this;
  }

  public ChatEntity type(ChatType type) {
    this.type = type;
    return this;
  }

  public ChatEntity lastReadInboxMessageId(Long lastReadInboxMessageId) {
    this.lastReadInboxMessageId = lastReadInboxMessageId;
    return this;
  }

  public ChatEntity lastReadOutboxMessageId(Long lastReadOutboxMessageId) {
    this.lastReadOutboxMessageId = lastReadOutboxMessageId;
    return this;
  }

  public ChatEntity messageAutoDeleteTime(Integer messageAutoDeleteTime) {
    this.messageAutoDeleteTime = messageAutoDeleteTime;
    return this;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    ChatEntity that = (ChatEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
  }

  @Override
  public String identifiableName() {
    return title;
  }

  @Getter
  @AllArgsConstructor
  public enum ChatType {
    PRIVATE(1579049844),
    BASIC_GROUP(973884508),
    SUPER_GROUP(-1472570774),
    SECRET(862366513);

    private final long id;
  }
}
