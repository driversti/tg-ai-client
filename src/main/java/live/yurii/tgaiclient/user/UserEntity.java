package live.yurii.tgaiclient.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import live.yurii.tgaiclient.common.JsonEscapable;
import live.yurii.tgaiclient.common.SenderEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.SqlTypes;

import java.util.Objects;

@Entity
@Table(name = "users")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends SenderEntity implements JsonEscapable {

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
  private UserType userType;

  public UserEntity(Long id) {
    super(id, SenderType.USER);
    this.userType = UserType.UNKNOWN;
  }

  public static UserEntity create(Long id) {
    return new UserEntity(id);
  }

  public UserEntity username(String username) {
    this.username = username;
    return this;
  }

  public UserEntity firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public UserEntity lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public UserEntity phoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

  public UserEntity contact(Boolean contact) {
    this.contact = contact;
    return this;
  }

  public UserEntity mutualContact(Boolean mutualContact) {
    this.mutualContact = mutualContact;
    return this;
  }

  public UserEntity premium(Boolean premium) {
    this.premium = premium;
    return this;
  }

  public UserEntity closeFriend(Boolean closeFriend) {
    this.closeFriend = closeFriend;
    return this;
  }

  public UserEntity restrictionReason(String restrictionReason) {
    this.restrictionReason = restrictionReason;
    return this;
  }

  public UserEntity languageCode(String languageCode) {
    this.languageCode = languageCode;
    return this;
  }

  public UserEntity userType(UserType userType) {
    this.userType = userType;
    return this;
  }

  public String identifiableName() {
    String fn = isNullOrEmpty(firstName) ? "" : firstName.trim();
    String ln = isNullOrEmpty(lastName) ? "" : lastName.trim();
    String u = isNullOrEmpty(username) ? "" : username.trim();
    String pn = isNullOrEmpty(phoneNumber) ? "" : phoneNumber.trim();
    String idStr = String.valueOf(id); // Safely convert Long to String

    String coreString;

    if (!fn.isEmpty() || !ln.isEmpty()) {
      // fn ln (u, id, pn)
      String namePart = (fn.isEmpty() ? "" : fn) + (fn.isEmpty() || ln.isEmpty() ? "" : " ") + (ln.isEmpty() ? "" : ln);
      coreString = String.format("%s (%s, %s, %s)", namePart, u, idStr, pn);

    } else if (!u.isEmpty()) {
      // u (id, pn)
      coreString = String.format("%s (%s, %s)", u, idStr, pn);
    } else if (!pn.isEmpty()) {
      // pn (id)
      coreString = String.format("%s (%s)", pn, idStr);
    } else {
      // id
      coreString = idStr;
    }

    return coreString.trim();
  }

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

  private static boolean isNullOrEmpty(String str) {
    return str == null || str.trim().isEmpty();
  }

  public enum UserType {
    REGULAR, DELETED, BOT, UNKNOWN
  }
}
