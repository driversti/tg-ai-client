package live.yurii.tgaiclient.handlers;

import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class UpdateManager implements Client.ResultHandler {

  private final ConcurrentMap<Integer, Set<UpdateHandler>> handlers = new ConcurrentHashMap<>();

  @Override
  public void onResult(TdApi.Object object) {
    int constructor = object.getConstructor();
    handlers.computeIfAbsent(constructor, k -> new HashSet<>())
        .forEach(handler -> {
          // TODO: Use GlobalExecutorHandler. Consider asynchronous execution
          try {
            handler.onResult(object);
          } catch (Exception e) {
            log.error("Error handling update for {} ({}): {}", object.getClass().getName(), constructor, e.getMessage(), e);
          }
        });
  }

  public void registerHandler(UpdateHandler handler) {
    int constructor = handler.forConstructor();
    handlers.computeIfAbsent(constructor, k -> new HashSet<>()).add(handler);
  }

  public void unregisterHandler(UpdateHandler handler) {
    int constructor = handler.forConstructor();
    Set<UpdateHandler> handlersSet = handlers.get(constructor);
    if (handlersSet != null) {
      handlersSet.remove(handler);
      if (handlersSet.isEmpty()) {
        handlers.remove(constructor);
      }
    } else {
      log.warn("Handler not found for constructor: {}", constructor);
    }
  }
}
