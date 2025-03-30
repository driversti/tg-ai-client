package live.yurii.tgaiclient.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserMapper {

  public UserEntity toEntity(TdApi.User user) {
    return UserEntity.create(user.id)
        .username(findUsername(user.usernames).orElse(null))
        .firstName(user.firstName)
        .lastName(user.lastName)
        .phoneNumber(user.phoneNumber)
        .contact(user.isContact)
        .mutualContact(user.isMutualContact)
        .premium(user.isPremium)
        .closeFriend(user.isCloseFriend)
        .restrictionReason(user.restrictionReason)
        .languageCode(user.languageCode)
        .userType(getUserType(user.type));
  }

  private Optional<String> findUsername(TdApi.Usernames usernames) {
    if (usernames == null) {
      return Optional.empty();
    }
    if (!isBlank(usernames.editableUsername)) {
      return Optional.ofNullable(usernames.editableUsername);
    }
    return Optional.ofNullable(usernames.activeUsernames)
        .map(arr -> arr[0]);
  }

  private UserEntity.UserType getUserType(TdApi.UserType type) {
    return switch (type.getConstructor()) {
      case TdApi.UserTypeRegular.CONSTRUCTOR -> UserEntity.UserType.REGULAR;
      case TdApi.UserTypeDeleted.CONSTRUCTOR -> UserEntity.UserType.DELETED;
      case TdApi.UserTypeBot.CONSTRUCTOR -> UserEntity.UserType.BOT;
      case TdApi.UserTypeUnknown.CONSTRUCTOR -> UserEntity.UserType.UNKNOWN;
      default -> throw new IllegalArgumentException("Unknown user type: " + type);
    };
  }

  public void updateEntity(UserEntity existingUser, TdApi.User user) {
    // map only if not removed to preserve the existing value
    findUsername(user.usernames).ifPresent(existingUser::username);
    Optional<TdApi.User> userOptional = Optional.of(user);
    userOptional.map(u -> u.firstName).ifPresent(existingUser::firstName);
    userOptional.map(u -> u.lastName).ifPresent(existingUser::lastName);
    userOptional.map(u -> u.phoneNumber).ifPresent(existingUser::phoneNumber);

    // map unconditionally
    existingUser.setContact(user.isContact);
    existingUser.setPremium(user.isPremium);
    existingUser.setCloseFriend(user.isCloseFriend);
    existingUser.setRestrictionReason(user.restrictionReason);
    existingUser.setLanguageCode(user.languageCode);
  }
}
