package swsec.bank.models;

public class Account {
  
  // Instance variables
  private int accountNum;
  private float currentBalance;

  public Account() {
    // constructor doesn't do anything, in the case when a new account is being created because the variables are not known yet. 
  }

  public Account (int accountNum, float currentBalance) {
    // in the case where an Account object is needed to represent an existing account, then the instance variables are populated.
    this.accountNum = accountNum;
    this.currentBalance = currentBalance;
  }

  public void setAccountNum (int accountNum) {
    this.accountNum = accountNum;
  }

  public void setCurrentBalance (float currentBalance) {
    this.currentBalance = currentBalance;
  }

  public int getAccountNum () {
    return this.accountNum;
  }

  public float getCurrentBalance () {
    return this.currentBalance;
  }

}
