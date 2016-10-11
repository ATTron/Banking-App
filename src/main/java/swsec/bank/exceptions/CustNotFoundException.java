package swsec.bank.exceptions;

// Exception thrown by the CustomerRepository class methods and handled in UiServices
//
public class CustNotFoundException extends Exception {

  private String username; 

  // constructor
  public CustNotFoundException (String username) {
    super ("Customer " + username + " does not exist.");
    this.username = username;
  }

  public String getUsername () {
    return username;
  }

}

