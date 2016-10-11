package swsec.bank.exceptions;

// Exception thrown by the CustomerController class methods and handled in UiServices
//
public class CustomerNotAuthenticatedException extends Exception {

  private String username; 

  // constructor
  public CustomerNotAuthenticatedException (String username) {
    super ("Customer " + username + " is not authenticated.");
    this.username = username;
  }

  public String getUsername () {
    return username;
  }

}

