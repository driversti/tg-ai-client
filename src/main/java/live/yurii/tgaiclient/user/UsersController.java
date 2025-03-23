package live.yurii.tgaiclient.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/users", consumes = "application/json", produces = "application/json")
@RequiredArgsConstructor
public class UsersController {

  private final UserService userService;

  @GetMapping("/known")
  public ResponseEntity<List<KnownUser>> getKnownUsers() {
    List<KnownUser> knownUsers = userService.getKnownUsers().stream()
        .map(UsersController::mapToKnownUsers)
        .toList();
    return ResponseEntity.ok(knownUsers);
  }

  private static KnownUser mapToKnownUsers(TdApi.User user) {
    String username = Optional.ofNullable(user.usernames).map(i -> i.activeUsernames).map(i -> i[0]).orElse("");
    return new KnownUser(user.id, username, user.firstName, user.lastName, user.phoneNumber, user.isPremium, user.isContact, user.isMutualContact);
  }

  public record KnownUser(long id, String username, String firstName, String lastName, String phone, boolean isPremium,
                          boolean isContact, boolean isMutualContact) {
  }
}
