package live.yurii.tgaiclient.chats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/chats", produces = "application/json")
@RequiredArgsConstructor
public class ChatsController {

  private final ChatsRestService service;

  @GetMapping("/{folderId}")
  ResponseEntity<ListChatsDTO> findByFolderId(@PathVariable String folderId) {
    log.debug("Fetching chats for folder {}", folderId);
    return ResponseEntity.ok(service.findByFolderId(Long.parseLong(folderId)));
  }
}
