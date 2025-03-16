package live.yurii.tgaiclient.authorization;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OtpCodeReceivedEvent extends ApplicationEvent {

  private final String code;

  public OtpCodeReceivedEvent(Object source, String code) {
    super(source);
    this.code = code;
  }
}
