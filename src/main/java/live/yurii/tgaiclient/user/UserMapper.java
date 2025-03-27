package live.yurii.tgaiclient.user;

import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Slf4j
@Component
public class UserMapper {
  // TODO: consider using MapStruct

  public UserDTO toDTO(UserEntity entity) {
    return UserDTO.builder()
        .id(entity.getId())
        .username(entity.getUsername())
        .firstName(entity.getFirstName())
        .lastName(entity.getLastName())
        .phone(entity.getPhoneNumber())
        .isPremium(entity.isPremium())
        .isContact(entity.isContact())
        .isMutualContact(entity.isMutualContact())
        .isCloseFriend(entity.isCloseFriend())
        .restrictionReason(entity.getRestrictionReason())
        .languageCode(entity.getLanguageCode())
        .userType(entity.getUserType())
        .build();
  }

  public UserEntity toEntity(TdApi.User user) {
    return UserEntity.create(user.id)
        .username(getUsername(user))
        .firstName(user.firstName)
        .lastName(user.lastName)
        .phoneNumber(user.phoneNumber)
        .contact(user.isContact)
        .mutualContact(user.isMutualContact)
        .premium(user.isPremium)
        .closeFriend(user.isCloseFriend)
        .restrictionReason(user.restrictionReason)
        .languageCode(user.languageCode)
        .userType(resolveUserType(user.type));
  }

  private static String getUsername(TdApi.User user) {
    return Optional.ofNullable(user.usernames)
        .map(u -> u.activeUsernames)
        .map(u -> u[0])
        .orElse(null);
  }

  private UserEntity.UserType resolveUserType(TdApi.UserType type) {
    return switch (type.getConstructor()) {
      case TdApi.UserTypeRegular.CONSTRUCTOR -> UserEntity.UserType.REGULAR;
      case TdApi.UserTypeDeleted.CONSTRUCTOR -> UserEntity.UserType.DELETED;
      case TdApi.UserTypeBot.CONSTRUCTOR -> UserEntity.UserType.BOT;
      case TdApi.UserTypeUnknown.CONSTRUCTOR -> UserEntity.UserType.UNKNOWN;
      default -> {
        log.warn("Unknown user type: {}", type);
        yield UserEntity.UserType.UNKNOWN;
      }
    };
  }

  public void updateEntity(UserEntity user, TdApi.UserFullInfo fullInfo) {
    // TODO: map bio, birthdate
  }

  public void updateEntity(UserEntity entity, TdApi.User user) {
    String username = getUsername(user);
    if (!isBlank(username)) {
      entity.setUsername(username);
    }
    if (!isBlank(user.firstName)) {
      entity.setFirstName(user.firstName);
    }
    if (!isBlank(user.lastName)) {
      entity.setLastName(user.lastName);
    }
    if (!isBlank(user.phoneNumber)) {
      entity.setPhoneNumber(user.phoneNumber);
    }
    entity.setContact(user.isContact);
    entity.setMutualContact(user.isMutualContact);
    entity.setPremium(user.isPremium);
    entity.setCloseFriend(user.isCloseFriend);
    entity.setRestrictionReason(user.restrictionReason);
    entity.setLanguageCode(user.languageCode);
    entity.setUserType(resolveUserType(user.type));
  }
}
