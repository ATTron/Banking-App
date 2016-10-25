package swsec.bank.controllers;

import swsec.bank.services.Credentials;
import swsec.bank.models.Customer;
import swsec.bank.services.repositories.CustomerRepository;
import swsec.bank.services.verificationSystem;
import swsec.bank.exceptions.CustNotFoundException;
import swsec.bank.exceptions.CustomerNoAccountException;
import swsec.bank.exceptions.AmountNotVerifiedException;
import swsec.bank.exceptions.CustomerNotAuthenticatedException;
import swsec.bank.exceptions.InsufficientFundsException;
import swsec.bank.exceptions.AccountNotFoundException;
import java.io.*;

public class CustomerController {

  private CustomerRepository thisRep; //the repository object associated with this customer
  private Customer thisCust;          // the model object associated with this customer
  private AccountController thisAcct; // the account controller associated with this customer's account, if the customer has one

  private verificationSystem VS = new verificationSystem ();

  public boolean hasAcct = false;  // indicates whether or not this customer has an account yet

  // constructor - called when a customer logs in, so username and password are known, but no other attributes are yet known
  // Assumes that all customers are created a priori; a new constructor will need to be added to create a new customer that does not previously exist
  public CustomerController(Credentials creds) throws CustNotFoundException, IOException  {
    
    int accountNum;

    // every instance of CustomerController needs a thisRep and a thisCust
    thisRep = new CustomerRepository();
    thisCust = new Customer(creds.getUsername(), creds.getPassword());

    //make sure it's a valid Customer
    try {
      accountNum = authenticate (thisRep, thisCust);

    } catch (IOException ex) {
      throw new IOException (); 
    }
    if (accountNum == -1) {      //customer was not found or password wrong
      throw new CustNotFoundException (creds.getUsername());
    }
    else {
      if (accountNum > 0)  //customer has an account 
      {
        //create AccountController object
        hasAcct = true;
        thisAcct = new AccountController (accountNum);
      }
    }
  }

  public boolean hasAcct () {

    return this.hasAcct;
  }


    
  // uses thisRep to lookup customer - if successful, will load customer attributes into tempCust and mark authenticated
  public int authenticate(CustomerRepository thisRep, Customer thisCust) throws IOException {
    Customer tempCust;

    try {
      tempCust = thisRep.lookup (thisCust);  //lookup will also check password

    } catch (IOException ex) {
      throw new IOException ();
    }

    if (tempCust.isAuthenticated ()) {
      thisCust = tempCust;

      return (tempCust.getAccountNum());  //will return 0 if there is no account number
    } else {
      return (-1);
    }
  }
   

  // when a new account is created for this Customer, most of the work is done in AdminController, but this object has to
  // store the account number in the Customer object 
  public void linkAccount(int accountNum, AccountController newAcct) throws CustNotFoundException, IOException {
    thisCust.setAccountNum(accountNum);
    thisAcct = newAcct;
    try {
      thisRep.update(thisCust);
    } catch (IOException ex) {
      throw new IOException ();
    } catch (CustNotFoundException ex) {
      throw new CustNotFoundException (thisCust.getUsername());
    }
  }


  // returns true if everything goes ok; otherwise throws exceptions
  public boolean makeDeposit (float amount) throws CustomerNoAccountException, AmountNotVerifiedException, CustomerNotAuthenticatedException, AccountNotFoundException, IOException {

    // checks to make sure thisCust is authenticated
    if (thisCust.isAuthenticated ()) {

      // checks verification system to verify amount
      if (this.VS.confirmAmount (amount)) {

        // check to make sure that this customer actually has an account, in which case thisAcct is already populated
        if (thisCust.getAccountNum() > 0) {
          try {
            thisAcct.changeBalance (thisAcct.getBalance (thisCust.getAccountNum()) + amount);
          } catch (IOException ex) {
            throw new IOException () ;
          } catch (AccountNotFoundException ex) {
            throw new AccountNotFoundException (thisCust.getAccountNum());
          }

          // return true 
          return (true);
        } else {
          // customer doesn't have an account
          throw new CustomerNoAccountException (thisCust.getUsername());
        } 
      } else {
        // verification system says amount is not correct
        throw new AmountNotVerifiedException (amount);
      } 
    } else {
      // customer is not authenticated
      throw new CustomerNotAuthenticatedException (thisCust.getUsername());
    }
  }


  // returns true if everything goes ok; otherwise throws exceptions
  public boolean makeWithdrawal (float amount) throws CustomerNoAccountException, AmountNotVerifiedException, CustomerNotAuthenticatedException, InsufficientFundsException, AccountNotFoundException, IOException {

    float currentBalance;

    // checks to make sure thisCust is authenticated
    if (thisCust.isAuthenticated ()) {

      // checks verification system to verify amount
      if (this.VS.confirmAmount (amount)) {

        // check to make sure that this customer actually has an account, in which case thisAcct is already populated
        if (thisCust.getAccountNum() > 0) {
          try {
            currentBalance = thisAcct.getBalance (thisCust.getAccountNum());
          } catch (IOException ex) {
            throw new IOException ();
          } catch (AccountNotFoundException ex) {
            throw new AccountNotFoundException (thisCust.getAccountNum());
          }

          if (amount < currentBalance) {
            try {
              thisAcct.changeBalance (thisAcct.getBalance (thisCust.getAccountNum()) - amount);
            } catch (AccountNotFoundException ex) {
              throw new AccountNotFoundException (thisCust.getAccountNum());
            }

            // return true 
            return (true); 
          } else {
            // customer doesn't have enough money in the account
            throw new InsufficientFundsException (thisCust.getUsername(), amount);
          } 
        } else {
          // customer doesn't have an account
          throw new CustomerNoAccountException (thisCust.getUsername());
        } 
      } else {
        // verification system says amount is not correct
        throw new AmountNotVerifiedException (amount);
      } 
    } else {
      // customer is not authenticated
      throw new CustomerNotAuthenticatedException (thisCust.getUsername());
    }
  }


  // returns the balance of this customer's account, if the customer is authenticated and has an account
  public float getBalance () throws IOException, CustomerNoAccountException, AccountNotFoundException {
    float balance = 0;

    // checks to make sure thisCust is authenticated
    if (thisCust.isAuthenticated ()) {

      // check to make sure that this customer actually has an account, in which case thisAcct is already populated
      if (thisCust.getAccountNum() > 0) {

        try {
          balance = thisAcct.getBalance(thisCust.getAccountNum());

        } catch (IOException ex) {
          throw new IOException ();
        } catch (AccountNotFoundException ex) {
          throw new AccountNotFoundException (thisCust.getAccountNum());
        }

      } else {
        // customer doesn't have an account
        throw new CustomerNoAccountException (thisCust.getUsername());
      }
    }
  return (balance);
  }

  //if this customer has an account, returns the AccountController object, otherwise throws an exception
  public AccountController getAcct() throws CustomerNoAccountException {
    if (hasAcct) {
      return thisAcct;
    } else {
      throw new CustomerNoAccountException (thisCust.getUsername());
    }
  }


  // marks a customer as not authenticated
  public void logout () {
    // marks thisCust as not authenticated
    thisCust.logout ();
  }
} 
