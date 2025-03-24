package live.yurii.tgaiclient.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users", consumes = "application/json", produces = "application/json")
public class UsersController {

  private final UserRestService service;

  @GetMapping("/all")
  public ResponseEntity<List<UserDTO>> getAllUsers() {
    log.debug("Fetching all users");
    List<UserDTO> users = service.findAll();
    log.debug("Found {} users", users.size());
    return ResponseEntity.ok(users);
  }
}
