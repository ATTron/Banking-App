package swsec.bank.exceptions;

// Exception thrown by the CustomerController class methods and handled in UiServices
//
public class InsufficientFundsException extends Exception {

  private String username;
  private Float amount; 

  // constructor
  public InsufficientFundsException(String username, Float amount) {
    super("Customer " + username 
        + " does not have sufficient funds to cover a withdrawal of " + Float.toString(amount));
    this.username = username;
    this.amount = amount;
  }

  public Float getAmount() {
    return amount;
  }

}

