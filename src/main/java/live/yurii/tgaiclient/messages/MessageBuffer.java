package live.yurii.tgaiclient.messages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Slf4j
class MessageBuffer {

  private final int capacity;
  private final ConcurrentLinkedDeque<Long> messageIds;
  private final ConcurrentHashMap<Long, BufferedMessage> messages;

  MessageBuffer(int capacity) {
    this.capacity = capacity;
    this.messageIds = new ConcurrentLinkedDeque<>();
    this.messages = new ConcurrentHashMap<>();
  }

  void addMessage(BufferedMessage message) {
    if (messageIds.size() >= capacity) {
      Long oldestId = messageIds.pollFirst();
      if (oldestId != null) {
        messages.remove(oldestId);
      }
    }
    messageIds.offerLast(message.getId());
    messages.put(message.getId(), message);
  }

  BufferedMessage getMessage(long id) {
    return messages.get(id);
  }

  int size() {
    return messageIds.size();
  }

  boolean containsMessage(long id) {
    return messages.containsKey(id);
  }

  Collection<BufferedMessage> findAll(Collection<Long> messageIds) {
    return messages.values()
        .stream().filter(m -> messageIds.contains(m.id))
        .toList();
  }

  void removeMessage(long id) {
    if (messages.containsKey(id)) {
      messages.remove(id);
      messageIds.remove(id);
    }
  }

  @Getter
  @RequiredArgsConstructor
  static class BufferedMessage {
    private final long id;

    private long chatId;
    private String chatTitle;
    private String author;
    private String content;

    BufferedMessage withChatId(long chatId) {
      this.chatId = chatId;
      return this;
    }

    BufferedMessage withChatTitle(String chatTitle) {
      this.chatTitle = chatTitle;
      return this;
    }

    BufferedMessage withAuthor(String author) {
      this.author = author;
      return this;
    }

    BufferedMessage withContent(String content) {
      this.content = content;
      return this;
    }
  }
}
