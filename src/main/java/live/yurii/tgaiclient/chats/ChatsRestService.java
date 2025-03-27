package live.yurii.tgaiclient.chats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatsRestService {

  private final ChatStorage storage;
  private final ChatMapper mapper;

  public ListChatsDTO findByFolderId(long folderId) {
    ListChatsDTO listChatsDTO = new ListChatsDTO();
    storage.getChatsByFolderId(folderId)
        .forEach(entity -> listChatsDTO.add(mapper.toDTO(entity)));
    return listChatsDTO;
  }
}
