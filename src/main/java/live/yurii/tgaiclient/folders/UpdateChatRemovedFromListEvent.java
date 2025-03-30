package live.yurii.tgaiclient.folders;

import live.yurii.tgaiclient.common.MainUpdateHandler;
import lombok.Getter;
import org.drinkless.tdlib.TdApi;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateChatRemovedFromListEvent extends ApplicationEvent {

  private final TdApi.UpdateChatRemovedFromList update;

  public UpdateChatRemovedFromListEvent(MainUpdateHandler source, TdApi.UpdateChatRemovedFromList update) {
    super(source);
    this.update = update;
  }
}
