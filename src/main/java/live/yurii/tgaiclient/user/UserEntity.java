package live.yurii.tgaiclient.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.function.Predicate.not;

@Getter
@Setter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class UserEntity {

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
  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(name = "user_type", nullable = false)
  private UserType userType;

  public UserEntity(Long id) {
    this.id = id;
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

  public String identifiableName() {
    if (username != null) {
      String fullName = Stream.of(firstName, lastName).filter(not(Strings::isBlank)).collect(Collectors.joining(" "));
      return format("https://t.me/%s ", username) +
          Stream.of(fullName, String.valueOf(id), phoneNumber)
              .filter(not(Strings::isBlank))
              .collect(Collectors.joining(", ", "(", ")"));

    }
    return format("%s %s", firstName, lastName) + Stream.of(String.valueOf(id), phoneNumber)
        .filter(not(Strings::isBlank))
        .collect(Collectors.joining(", ", "(", ")"));
  }

  public enum UserType {
    REGULAR, DELETED, BOT, UNKNOWN
  }
}
