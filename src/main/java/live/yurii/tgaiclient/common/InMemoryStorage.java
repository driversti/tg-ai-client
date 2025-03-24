package live.yurii.tgaiclient.common;

import lombok.extern.slf4j.Slf4j;
import org.drinkless.tdlib.TdApi;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
public class InMemoryStorage implements Storage {

  private final ConcurrentMap<Long, TdApi.User> users = new ConcurrentHashMap<>();
  private final ConcurrentMap<Long, TdApi.BasicGroup> basicGroups = new ConcurrentHashMap<>();
  private final ConcurrentMap<Long, TdApi.Supergroup> supergroups = new ConcurrentHashMap<>();
  private final ConcurrentMap<Integer, TdApi.SecretChat> secretChats = new ConcurrentHashMap<>();
  private final ConcurrentMap<Long, TdApi.Chat> chats = new ConcurrentHashMap<>();
  private final NavigableSet<OrderedChat> mainChatList = new TreeSet<>();
  private final ConcurrentMap<Long, TdApi.UserFullInfo> usersFullInfo = new ConcurrentHashMap<>();
  private final ConcurrentMap<Long, TdApi.BasicGroupFullInfo> basicGroupsFullInfo = new ConcurrentHashMap<>();
  private final ConcurrentMap<Long, TdApi.SupergroupFullInfo> supergroupsFullInfo = new ConcurrentHashMap<>();
  private boolean haveFullMainChatList = false;

  @Override
  public void putSuperGroup(TdApi.Supergroup supergroup) {
    supergroups.put(supergroup.id, supergroup);
  }

  @Override
  public void putChat(TdApi.Chat chat) {
    synchronized (chat) {
      chats.put(chat.id, chat);

      TdApi.ChatPosition[] positions = chat.positions;
      chat.positions = new TdApi.ChatPosition[0];
      setChatPositions(chat, positions);
    }
  }

  @Override
  public Optional<TdApi.Chat> findChat(long id) {
    return Optional.ofNullable(chats.get(id));
  }

  @Override
  public String chatTitle(long chatId) {
    return Optional.ofNullable(chats.get(chatId)).map(chat -> chat.title).orElse("");
  }

  @Override
  public void putUser(TdApi.User user) {
    users.put(user.id, user);
  }

  @Override
  public Optional<TdApi.User> findUser(long userId) {
    return Optional.ofNullable(users.get(userId));
  }

  @Override
  public Collection<TdApi.User> getUsers() {
    return users.values().stream().toList();
  }

  @Override
  public void updateUserStatus(long userId, TdApi.UserStatus status) {
    users.computeIfPresent(userId, (id, user) -> {
      user.status = status;
      return user;
    });
  }

  @Override
  public void putBasicGroup(TdApi.BasicGroup basicGroup) {
    basicGroups.put(basicGroup.id, basicGroup);
  }

  @Override
  public void putSecretChat(TdApi.SecretChat secretChat) {
    secretChats.put(secretChat.id, secretChat);
  }

  private void setChatPositions(TdApi.Chat chat, TdApi.ChatPosition[] positions) {
    synchronized (mainChatList) {
        for (TdApi.ChatPosition position : chat.positions) {
          if (position.list.getConstructor() == TdApi.ChatListMain.CONSTRUCTOR) {
            boolean isRemoved = mainChatList.remove(new OrderedChat(chat.id, position));
            if (!isRemoved) {
              log.warn("Failed to remove chat {} ({}) from mainChatList", chat.title, chat.id);
            }
          }
        }

        chat.positions = positions;

        for (TdApi.ChatPosition position : chat.positions) {
          if (position.list.getConstructor() == TdApi.ChatListMain.CONSTRUCTOR) {
            boolean isAdded = mainChatList.add(new OrderedChat(chat.id, position));
            if (!isAdded) {
              log.warn("Failed to add chat {} ({}) to mainChatList", chat.title, chat.id);
            }
          }
        }
    }
  }

  private record OrderedChat(long chatId, TdApi.ChatPosition position) implements Comparable<OrderedChat> {

    @Override
    public int compareTo(OrderedChat o) {
      if (this.position.order != o.position.order) {
        return o.position.order < this.position.order ? -1 : 1;
      }
      if (this.chatId != o.chatId) {
        return o.chatId < this.chatId ? -1 : 1;
      }
      return 0;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null || getClass() != obj.getClass()) return false;
      OrderedChat o = (OrderedChat) obj;
      return this.chatId == o.chatId && this.position.order == o.position.order;
    }
  }
}
