package live.yurii.tgaiclient.folders;

import live.yurii.tgaiclient.chats.ChatDTO;
import lombok.Builder;

import java.util.Collection;

@Builder
public record FolderDTO(Integer id, String name, Collection<ChatDTO> chats) {
}
