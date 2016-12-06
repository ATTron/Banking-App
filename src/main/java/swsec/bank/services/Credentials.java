package swsec.bank.services;

public class Credentials {

  public String username;
  public String password;

  public Credentials(String username, String password) {
    setUsername(username);
    setPassword(password);
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
