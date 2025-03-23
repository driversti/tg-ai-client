package live.yurii.tgaiclient.user;

import lombok.Builder;

@Builder
public record UserDTO(long id, String username, String firstName, String lastName, String phone, Boolean isPremium,
               Boolean isContact, Boolean isMutualContact, Boolean isCloseFriend, String restrictionReason,
               String languageCode, UserEntity.UserType userType) {
}
