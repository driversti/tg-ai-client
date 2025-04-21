package live.yurii.tgaiclient.folders;

import java.util.Map;

public record ChatInFolderResponse(long folderId, String sessionId, Map<Long, String> chats) {

}
