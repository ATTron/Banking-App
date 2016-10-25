package swsec.bank.controllers;

import swsec.bank.services.Credentials;
import swsec.bank.models.Admin;
import swsec.bank.models.Customer;
import swsec.bank.services.repositories.AdminRepository;
import swsec.bank.services.repositories.CustomerRepository;
import swsec.bank.exceptions.AccountNotFoundException;
import java.io.*;

public class AdminController {

  private AdminRepository thisRep;
  private Admin thisAdmin;

  public AdminController() {
    // every instance of a Controller needs a corresponding Repository and Model object
    thisRep = new AdminRepository();
    thisAdmin = new Admin();
  }


  // uses thisRep to look up Admin - if successful, will load Admin attributes into thisAdmin
  // and mark authenticated
  public boolean authenticate(Credentials creds) {
    thisAdmin.setCredentials(creds.getUsername(), creds.getPassword());
    Admin tempAdmin = thisRep.lookup (thisAdmin);
    if (tempAdmin.isAuthenticated ()) {
      thisAdmin = tempAdmin;
      return (true); 
    } else {
      return (false);
    } 
  }

  public int requestAccount (float initialBalance) {
    // called by CustomerController
    // needs to create an instance of UiService, which authenticates the Admin
    // then needs to create an instance of an AccountController, which it uses to create the new account

    // TBD
    // UiService thisUIS = new UiService ();
    // thisUIS.authenticateAdmin ();
    AccountController thisNewAcct = new AccountController (0);
    return thisNewAcct.newAccount (initialBalance);
  }

  public void modifyBalance (int accountNum, float newBalance) throws IOException, AccountNotFoundException {
    // checks to make sure this Admin object is authenticated
    // creates an instance of AccountController and uses it to modify the balance
    if (thisAdmin.isAuthenticated ()) {
      AccountController thisAcct = new AccountController(accountNum);
      try {
        thisAcct.changeBalance (newBalance);
      } catch (IOException ex) {
        throw new IOException ();
      } catch (AccountNotFoundException acctEx) {
        throw new AccountNotFoundException (accountNum);
      }
    } else {
      System.out.println ("Admin needs to be authenticated first.");
    }
  }
}
