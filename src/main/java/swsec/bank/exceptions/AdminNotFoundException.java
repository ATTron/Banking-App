package swsec.bank.exceptions;

// Exception thrown by the AdminRepository class methods and handled in UiServices
//
public class AdminNotFoundException extends Exception {

  private String username; 

  // constructor
  public AdminNotFoundException (String username) {
    super ("Admin " + username + " does not exist.");
    this.username = username;
  }

  public String getUsername () {
    return username;
  }

}

