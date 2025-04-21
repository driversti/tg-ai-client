package live.yurii.tgaiclient.folders;

import live.yurii.tgaiclient.chats.ChatEntity;
import live.yurii.tgaiclient.chats.ChatRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpMethod.POST;

@Slf4j
@Service
@RequiredArgsConstructor
class FolderService {

  private final FolderRepository folderRepository;
  private final FolderMapper folderMapper;
  private final Client telegramClient;
  private final ChatRepository chatRepository;
  private final RestTemplate n8nClient;

  List<FolderResponse> findAll() {
    return folderRepository.findAll()
        .stream()
        .map(folderMapper::toFolderResponse)
        .toList();
  }

  void requestChatsInFolder(int folderId, String sessionId) {
    telegramClient.send(new TdApi.GetChatFolder(folderId), new SendChatsToN8n(folderId, sessionId));
  }

  @AllArgsConstructor
  private class SendChatsToN8n implements Client.ResultHandler {
    private final int folderId;
    private final String sessionId;

    @Override
    public void onResult(TdApi.Object object) {
      if (object.getConstructor() != TdApi.ChatFolder.CONSTRUCTOR) {
        log.error("Failed to get folder with id {}: {}", folderId, object);
        // consider notifying the user about the error in n8n
        return;
      }
      List<Long> chatIds = Arrays.stream(((TdApi.ChatFolder) object).includedChatIds).boxed().toList();
      Map<Long, String> chats = chatRepository.findAllByIdIn(chatIds).stream()
          .collect(Collectors.toMap(ChatEntity::getId, ChatEntity::getTitle));
      ChatInFolderResponse payload = new ChatInFolderResponse(folderId, sessionId, chats);
      ResponseEntity<Void> response = n8nClient.exchange("/webhook/chats", POST, new HttpEntity<>(payload), Void.class);
      if (response.getStatusCode().is2xxSuccessful()) {
        log.debug("Successfully sent {} chats in folder {} to n8n", payload.chats().size(), folderId);
      } else {
        log.error("Failed to send chats in folder {} to n8n: {}", folderId, response.getStatusCode());
      }
    }
  }
}
