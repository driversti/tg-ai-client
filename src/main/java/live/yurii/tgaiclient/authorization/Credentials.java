package live.yurii.tgaiclient.authorization;

public record Credentials(int developerId, String phoneNumber, String email, String password, int apiId, String apiHash) {
  public Credentials {
    if ((phoneNumber == null && email == null) || password == null || apiId <= 0 || apiHash == null) {
      throw new IllegalArgumentException("Invalid credentials");
    }
  }
}
