package live.yurii.tgaiclient.common;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "senders")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class SenderEntity {

  @Id
  @Column(name = "sender_id", nullable = false, updatable = false)
  protected Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "sender_type", nullable = false, updatable = false)
  protected SenderType senderType;

  public enum SenderType {
    USER, CHAT
  }
}
