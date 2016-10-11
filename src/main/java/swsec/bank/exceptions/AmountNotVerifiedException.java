package swsec.bank.exceptions;

// Exception thrown by the CustomerController class methods and handled in UiServices
//
public class AmountNotVerifiedException extends Exception {

  private Float amount; 

  // constructor
  public AmountNotVerifiedException (Float amount) {
    super ("Amount of " + amount + " has not been verified.");
    this.amount = amount;
  }

  public Float getAmount () {
    return amount;
  }

}

