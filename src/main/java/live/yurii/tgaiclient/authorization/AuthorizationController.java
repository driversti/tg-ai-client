package live.yurii.tgaiclient.authorization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthorizationController {

  private final AuthorizationHandler authorizationHandler;

  @GetMapping("/login")
  ResponseEntity<String> init() {
    log.info("Authorization request received.");
    authorizationHandler.init();
    return ResponseEntity.accepted().body("Authorization initialized. Use POST /login/otp when receive.");
  }

  @PostMapping("/otp")
  ResponseEntity<String> loginWithOtp(@RequestBody String code) {
    log.info("Received otp code: {}", code);
    authorizationHandler.loginWithOtp(code);
    return ResponseEntity.accepted().build();
  }

  @GetMapping("/logout")
  ResponseEntity<String> logout() {
    log.info("Logging out...");
    authorizationHandler.logout();
    return ResponseEntity.accepted().body("Logout request accepted.");
  }
}
