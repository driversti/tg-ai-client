package live.yurii.tgaiclient.authorization;

import org.springframework.context.ApplicationEvent;

public class LoginRequestEvent extends ApplicationEvent {
  public LoginRequestEvent(Object source) {
    super(source);
  }
}
