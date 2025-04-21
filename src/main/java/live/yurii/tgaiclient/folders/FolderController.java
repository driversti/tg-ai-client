package live.yurii.tgaiclient.folders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/folders", produces = MediaType.APPLICATION_JSON_VALUE)
public class FolderController {

  private final FolderService folderService;

  @GetMapping
  public ResponseEntity<List<FolderResponse>> findAll() {
    List<FolderResponse> folders = folderService.findAll();
    if (folders.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(folders);
  }

  @PostMapping("/chats")
  public ResponseEntity<Void> requestChatsInFolder(@RequestBody GetChatsInFolderRequest request) {
    folderService.requestChatsInFolder(request.folderId() , request.sessionId());

    return ResponseEntity.accepted().build();
  }
}
