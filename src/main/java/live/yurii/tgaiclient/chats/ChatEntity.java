package live.yurii.tgaiclient.chats;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.type.SqlTypes;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Audited
@AuditTable(value = "chat_history")
@Entity
@Table(name = "chats")
public class ChatEntity {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @Column(name = "title")
  private String title;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(name = "chat_type")
  private ChatType type;

  @NotAudited
  @Column(name = "last_read_inbox_message_id")
  private Long lastReadInboxMessageId;

  @NotAudited
  @Column(name = "last_read_outbox_message_id")
  private Long lastReadOutboxMessageId;

  @NotAudited
  @Column(name = "message_auto_delete_time")
  private Integer messageAutoDeleteTime;

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ChatEntity that)) return false;
    return Objects.equals(getId(), that.getId())
        && Objects.equals(getTitle(), that.getTitle())
        && getType() == that.getType()
        && Objects.equals(getLastReadInboxMessageId(), that.getLastReadInboxMessageId())
        && Objects.equals(getLastReadOutboxMessageId(), that.getLastReadOutboxMessageId())
        && Objects.equals(getMessageAutoDeleteTime(), that.getMessageAutoDeleteTime());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getTitle(), getType(), getLastReadInboxMessageId(), getLastReadOutboxMessageId(),
        getMessageAutoDeleteTime());
  }

  public enum ChatType {
    PRIVATE,
    SECRET,
    BASIC_GROUP,
    SUPERGROUP,
  }
}
