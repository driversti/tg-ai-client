package live.yurii.tgaiclient.me;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class GetMeController {

  private final GetMeService getMeService;

  @GetMapping
  public void getMe() {
    log.debug("getMe request received");
    getMeService.getMe();
  }
}
