package live.yurii.tgaiclient.folders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoldersRestService {

  private final FolderStorage storage;
  private final FolderMapper mapper;

  public List<FolderDTO> findAll() {
    return storage.findAll().stream()
        .map(mapper::toDTO)
        .toList();
  }
}
