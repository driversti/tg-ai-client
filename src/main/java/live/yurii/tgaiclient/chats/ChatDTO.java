package live.yurii.tgaiclient.chats;

import lombok.Builder;

@Builder
public record ChatDTO(long id, String title) {
}
