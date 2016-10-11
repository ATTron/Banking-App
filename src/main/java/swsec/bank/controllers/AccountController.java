package swsec.bank.controllers;

import swsec.bank.models.Account;
import swsec.bank.models.Admin;
import swsec.bank.services.repositories.AccountRepository;
import swsec.bank.services.repositories.AdminRepository;
import java.util.Random;
import swsec.bank.exceptions.AccountNotFoundException;
import java.io.*;

public class AccountController {

  private AccountRepository thisRep;
  private Account thisAccount;
  private Random rnd;

  public AccountController(int accountNum) {
    // every instance of a Controller needs a corresponding Repository and Model object
    thisRep = new AccountRepository();
    thisAccount = new Account();
    rnd = new Random ();
    thisAccount.setAccountNum(accountNum);  //might be 0 if there is no acct# yet
  }


  public void changeBalance(float newBalance) throws IOException, AccountNotFoundException {
    this.thisAccount.setCurrentBalance(newBalance); //put the new balance in the model object

    try {
    this.thisRep.modifyBalance (thisAccount, newBalance);  // put the new balance in the repository
    } catch (IOException ex) {
      throw new IOException ();
    } catch (AccountNotFoundException accEx) {
      throw new AccountNotFoundException (thisAccount.getAccountNum());
    }
  }

  public int newAccount(float newBalance) {
    // create new account number and add it to thisAccount
    // use thisRep to write new account to the file
    // return account #

    //produces an integer between 1000 and 9999
    int newAccountNum = 1000 + (rnd.nextInt (8999)); 
    
    thisAccount.setAccountNum (newAccountNum);
    thisAccount.setCurrentBalance (newBalance);
    thisRep.save (thisAccount);

    return (newAccountNum);
  
  }

  public float getBalance (int accountNum) throws IOException, AccountNotFoundException { 
    // Look up the account number and return the associated balance
   
    float balance = 0;
    try {

      balance = thisRep.getBalance (accountNum);

    } catch (IOException ex) {
      throw new IOException ();
    } finally {
      return (balance);
    }
  }

}
