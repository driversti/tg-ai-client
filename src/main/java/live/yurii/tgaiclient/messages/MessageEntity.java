package live.yurii.tgaiclient.messages;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.Instant;
import java.util.Objects;

@Audited
@AuditTable(value = "message_history")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "messages")
public class MessageEntity {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @NotAudited
  @Column(name = "sender_id")
  private Long senderId;

  @NotAudited
  @Column(name = "is_channel_post")
  private Boolean isChannelPost;

  @NotAudited
  @Column(name = "is_topic_message")
  private Boolean isTopicMessage;

  @NotAudited
  @Column(name = "date")
  private Instant date;

  @NotAudited
  @Column(name = "edit_date")
  private Instant editDate;

  @NotAudited
  @Column(name = "via_bot_id")
  private Long viaBotId;

  @Column(name = "text")
  private String text;

  public MessageEntity(Long id, Long senderId) {
    this.id = id;
    this.senderId = senderId;
  }

  public static MessageEntity create(Long id, Long senderId) {
    return new MessageEntity(id, senderId);
  }

  public MessageEntity isChannelPost(Boolean isChannelPost) {
    this.isChannelPost = isChannelPost;
    return this;
  }

  public MessageEntity isTopicMessage(Boolean isTopicMessage) {
    this.isTopicMessage = isTopicMessage;
    return this;
  }

  public MessageEntity date(Instant date) {
    this.date = date;
    return this;
  }

  public MessageEntity date(int date) {
    this.date = Instant.ofEpochSecond(date);
    return this;
  }

  public MessageEntity editDate(Instant editDate) {
    this.editDate = editDate;
    return this;
  }

  public MessageEntity editDate(int editDate) {
    this.editDate = Instant.ofEpochSecond(editDate);
    return this;
  }

  public MessageEntity viaBotId(Long viaBotId) {
    this.viaBotId = viaBotId;
    return this;
  }

  public MessageEntity text(String text) {
    this.text = text;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof MessageEntity that)) return false;
    return Objects.equals(getId(), that.getId())
        && Objects.equals(getSenderId(), that.getSenderId())
        && Objects.equals(getIsChannelPost(), that.getIsChannelPost())
        && Objects.equals(getIsTopicMessage(), that.getIsTopicMessage())
        && Objects.equals(getDate(), that.getDate())
        && Objects.equals(getEditDate(), that.getEditDate())
        && Objects.equals(getViaBotId(), that.getViaBotId())
        && Objects.equals(getText(), that.getText());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getSenderId(), getIsChannelPost(), getIsTopicMessage(), getDate(), getEditDate(),
        getViaBotId(), getText());
  }
}
