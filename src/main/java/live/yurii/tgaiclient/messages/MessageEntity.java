package live.yurii.tgaiclient.messages;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import live.yurii.tgaiclient.common.SenderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "messages")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "sender_id", nullable = false, updatable = false)
  private SenderEntity sender;

  @Column(name = "is_channel_post")
  private Boolean isChannelPost;

  @Column(name = "is_topic_message")
  private Boolean isTopicMessage;

  @Column(name = "date")
  private Instant date;

  @Column(name = "edit_date")
  private Instant editDate;

  @Column(name = "via_bot_id")
  private Long viaBotId;

  @Column(name = "content_text")
  private String contentText;

  @ToString.Exclude
  @Column(name = "content_embedding", columnDefinition = "vector(768)")
  private float[] contentEmbedding;

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    MessageEntity that = (MessageEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
  }

  public void setContentText(String contentText) {
    this.contentText = contentText;
    this.contentEmbedding = null;
  }
}
