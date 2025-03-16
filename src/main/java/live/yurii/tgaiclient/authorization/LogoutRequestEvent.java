package live.yurii.tgaiclient.authorization;

import org.springframework.context.ApplicationEvent;

public class LogoutRequestEvent extends ApplicationEvent {
  public LogoutRequestEvent(Object source) {
    super(source);
  }
}
