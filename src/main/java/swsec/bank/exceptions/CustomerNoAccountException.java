package swsec.bank.exceptions;

// Exception thrown by the CustomerController class methods and handled in UiServices
//
public class CustomerNoAccountException extends Exception {

  private String username; 

  // constructor
  public CustomerNoAccountException(String username) {
    super("Customer " + username + " does not have an account.");
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

}

