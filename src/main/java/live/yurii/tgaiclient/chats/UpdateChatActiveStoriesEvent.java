package live.yurii.tgaiclient.chats;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UpdateChatActiveStoriesEvent extends ApplicationEvent {

  private final TdApi.UpdateChatActiveStories update;

  public UpdateChatActiveStoriesEvent(Object source, TdApi.UpdateChatActiveStories update) {
    super(source);
    this.update = update;
  }
}
