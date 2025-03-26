package live.yurii.tgaiclient.folders;

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
@RequestMapping(value = "/folders", produces = "application/json")
public class FoldersController {

  private final FoldersRestService service;

  @GetMapping("/all")
  public ResponseEntity<List<ListFoldersDTO>> listFolders() {
    log.debug("Fetching all folders");
    return ResponseEntity.ok(service.findAll());
  }
}
