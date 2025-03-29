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
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
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

  @Column(name = "last_read_inbox_message_id")
  private Long lastReadInboxMessageId;

  @Column(name = "last_read_outbox_message_id")
  private Long lastReadOutboxMessageId;

  @Column(name = "message_auto_delete_time")
  private Integer messageAutoDeleteTime;

  public enum ChatType {
    PRIVATE,
    SECRET,
    BASIC_GROUP,
    SUPERGROUP,
  }
}
