package live.yurii.tgaiclient.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import live.yurii.tgaiclient.common.JsonEscapable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.SqlTypes;

import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements JsonEscapable { // Implement the interface

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @Column(name = "username")
  private String username;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Getter(AccessLevel.PRIVATE)
  @Column(name = "is_contact")
  private Boolean contact;

  @Getter(AccessLevel.PRIVATE)
  @Column(name = "is_mutual_contact")
  private Boolean mutualContact;

  @Getter(AccessLevel.PRIVATE)
  @Column(name = "is_premium")
  private Boolean premium;

  @Getter(AccessLevel.PRIVATE)
  @Column(name = "is_close_friend")
  private Boolean closeFriend;

  @Column(name = "restriction_reason")
  private String restrictionReason;

  @Column(name = "language_code", length = 5)
  private String languageCode;

  @Enumerated(EnumType.STRING)
  @Column(name = "user_type", nullable = false)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  private UserType userType = UserType.UNKNOWN;

  @Override
  public String toString() {
    return "{" +
        "\"id\":" + id + "," +
        "\"username\":" + (username == null ? "null" : "\"" + escapeJsonString(username) + "\"") + "," +
        "\"firstName\":" + (firstName == null ? "null" : "\"" + escapeJsonString(firstName) + "\"") + "," +
        "\"lastName\":" + (lastName == null ? "null" : "\"" + escapeJsonString(lastName) + "\"") + "," +
        "\"phoneNumber\":" + (phoneNumber == null ? "null" : "\"" + escapeJsonString(phoneNumber) + "\"") + "," +
        "\"isContact\":" + contact + "," +
        "\"isMutualContact\":" + mutualContact + "," +
        "\"isPremium\":" + premium + "," +
        "\"isCloseFriend\":" + closeFriend + "," +
        "\"restrictionReason\":" + (restrictionReason == null ? "null" : "\"" + escapeJsonString(restrictionReason) + "\"") + "," +
        "\"languageCode\":" + (languageCode == null ? "null" : "\"" + escapeJsonString(languageCode) + "\"") + "," +
        "\"userType\":" + (userType == null ? "null" : "\"" + userType + "\"") +
        "}";
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    UserEntity that = (UserEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ?
        ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() :
        getClass().hashCode();
  }

  public Boolean isContact() {
    return contact;
  }

  public Boolean isMutualContact() {
    return mutualContact;
  }

  public Boolean isPremium() {
    return premium;
  }

  public Boolean isCloseFriend() {
    return closeFriend;
  }

  public enum UserType {
    REGULAR, DELETED, BOT, UNKNOWN
  }
}
