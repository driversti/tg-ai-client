package live.yurii.tgaiclient.chats;

import lombok.Getter;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateChatIsTranslatableEvent extends ApplicationEvent {

  private final TdApi.UpdateChatIsTranslatable update;

  public UpdateChatIsTranslatableEvent(Object source, TdApi.UpdateChatIsTranslatable update) {
    super(source);
    this.update = update;
  }
}
