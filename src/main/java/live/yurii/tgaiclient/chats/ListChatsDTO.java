package live.yurii.tgaiclient.chats;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class ListChatsDTO {

  private final Collection<ChatDTO> chats = new ArrayList<>();

  public void add(ChatDTO chat) {
    chats.add(chat);
  }
}
