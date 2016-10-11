package swsec.bank.exceptions;

// Exception thrown by the AccountRepository class methods and handled in UiServices
//
public class AccountNotFoundException extends Exception {

  private int accountNum;

  // constructor
  public AccountNotFoundException (int accountNum) {
    super ("Account " + Integer.toString(accountNum) + " does not exist.");
    this.accountNum = accountNum;
  }

  public int getAccountNum () {
    return accountNum;
  }

}

