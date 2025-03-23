package live.yurii.tgaiclient.user;

import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
    return UserEntity.builder()
        .id(user.id)
        .username(Optional.ofNullable(user.usernames).map(u -> u.activeUsernames).map(u -> u[0]).orElse(null))
        .firstName(user.firstName)
        .lastName(user.lastName)
        .phoneNumber(user.phoneNumber)
        .contact(user.isContact)
        .mutualContact(user.isMutualContact)
        .premium(user.isPremium)
        .closeFriend(user.isCloseFriend)
        .restrictionReason(user.restrictionReason)
        .languageCode(user.languageCode)
        .userType(resolveUserType(user.type))
        .build();
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
}
