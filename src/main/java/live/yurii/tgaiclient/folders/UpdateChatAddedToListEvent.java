package live.yurii.tgaiclient.folders;

import live.yurii.tgaiclient.common.MainUpdateHandler;
import lombok.Getter;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateChatAddedToListEvent extends ApplicationEvent {

  private final TdApi.UpdateChatAddedToList update;

  public UpdateChatAddedToListEvent(MainUpdateHandler source, TdApi.UpdateChatAddedToList update) {
    super(source);
    this.update = update;
  }
}
