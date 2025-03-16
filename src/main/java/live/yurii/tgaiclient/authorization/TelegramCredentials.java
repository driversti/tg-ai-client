package live.yurii.tgaiclient.authorization;

public record TelegramCredentials(int developerId, String phoneNumber, String email, String password, int apiId,
                                  String apiHash, String botToken) {

}
