package swsec.bank.services;

import swsec.bank.controllers.AccountController;
import swsec.bank.controllers.AdminController;
import swsec.bank.controllers.CustomerController;

import swsec.bank.exceptions.AccountNotFoundException;
import swsec.bank.exceptions.AdminNotFoundException;
import swsec.bank.exceptions.AmountNotVerifiedException;
import swsec.bank.exceptions.CustNotFoundException;
import swsec.bank.exceptions.CustomerNoAccountException;
import swsec.bank.exceptions.CustomerNotAuthenticatedException;
import swsec.bank.exceptions.InsufficientFundsException;
import swsec.bank.exceptions.MaliciousInputException;
import swsec.bank.services.Credentials;


import java.io.IOException;
import java.io.Console;
import java.util.Scanner;
import java.util.logging.*;

public class UiService implements Runnable {

  CustomerController thisCust;   
  // represents the Customer currently interacting with the UI
  AccountController thisAcct;    
  // represents the Account belonging to the customer currently interacting with the UI
  AdminController thisAdmin;     
  // represents the Admin currently interacting with the UI
  verificationSystem VS = new verificationSystem();
  public static boolean isUser = false;
  public static boolean wantsToQuit = false;
  public static String wipe = "";
  private static final Logger LOGGER = Logger.getLogger(UiService.class.getName());

  public UiService() {

    LOGGER.setUseParentHandlers(false);

    Handler fileHandler = null;

    try {
      fileHandler = new FileHandler("./IS472.log");
      fileHandler.setFormatter(new SimpleFormatter());

      LOGGER.addHandler(fileHandler);
      LOGGER.config("Configuration done.");
      LOGGER.log(Level.FINE, "Finer logged");
    } catch (IOException exception) {
      LOGGER.log(Level.SEVERE, "Error occur in FileHandler.", exception);
    } finally { } 

    LOGGER.info("Logger Name: " + LOGGER.getName());
  }

  //This gets called by main() in class Runner
  public void run() {
    runUI();
  }


  public void runUI() {
    Scanner scanner = null;
    boolean quit = false;
    quit = initialLogin(); // initialLogin returns true if user wants to quit
    while (!quit) {
      quit = showMenu();  // showMenu returns true if user wants to quit
    }
    if (scanner != null) {
      scanner.close();
    }
  }


  public static String stripHtml(String inStr) {
    boolean inTag = false;
    char c;
    int tagPos = 0;
    StringBuffer outStr = new StringBuffer();
    int len = inStr.length();
    for (int i = 0; i < len; i++) {
      c = inStr.charAt(i);
      if (c == '<') {
        outStr.append("");
        tagPos = 0;
        inTag = true;
      }
      if (!inTag) {
        outStr.append(c);
      }
      if (c == '>') {
        inTag = false;
        if (tagPos == 1) {
          outStr.append(" <> ");
          tagPos = 0;
        }
      }
      tagPos += 1;
    }
    return outStr.toString();
  }


  public static final String BLACK_LIST_INPUT = "*[]#$";

  public static boolean isBlackList(String val) {
    if (val == null) {
      return true;
    }
    for (int i = 0; i < val.length(); i++) {
      if (BLACK_LIST_INPUT.indexOf(val.charAt(i)) != -1) {
        return true;
      }
    }
    return false;
  }

  public static final String SQLI_CHARACTER_CHECK = "-%+=&";

  public static boolean detectSQLInjection(String input) {
    if (input == null) {
      return true;
    }
    for (int i = 0; i < input.length(); i++) {
      if (SQLI_CHARACTER_CHECK.indexOf(input.charAt(i)) != -1) {
        return true;
      }
    }
    return false;
  }



  public static String cleanseInput(String input) throws MaliciousInputException {
    wipe = stripHtml(input);
    if (isBlackList(wipe) == true || detectSQLInjection(wipe) == true) {
      throw new MaliciousInputException();
    }
    return wipe;
  }

  protected boolean initialLogin() {
    // allow user to try to log in as many times as they want

    //boolean wantsToQuit = false;
    boolean loggedIn = false;
    String inUsername = "";   //the username typed in
    String inPassword = "";   // the password typed in
    Scanner scanner = new Scanner(System.in, "UTF-8");

    while (!wantsToQuit && !loggedIn) {

      System.out.println("Please type your username or EXIT to quit:");

      inUsername = scanner.next();
      try {
        inUsername = cleanseInput(inUsername);
      } catch (MaliciousInputException e) {
        if (isUser == true) {
          System.err.println(e.toStringUser());
          LOGGER.log(Level.SEVERE, e + " " + wipe);
          return true;
        } else {
          System.err.println(e + " " + wipe);
          LOGGER.log(Level.SEVERE, e + " " + wipe);
          return true;
        }
      } finally { }

      wantsToQuit = (inUsername.compareTo("EXIT") == 0);

      if (!wantsToQuit) {
        // using console to get access to readPassword() method
        Console console = System.console();
        if (console == null){
          System.out.println("Console not active !");
          System.exit(0);
        }
        inPassword = new String(console.readPassword("Please type your password: "));
        try {
          inPassword = cleanseInput(inPassword); 
        } catch (MaliciousInputException e) {
          if (isUser == true) {
            System.err.println(e.toStringUser());
            LOGGER.log(Level.SEVERE, e + " " + wipe);
            return true;
          } else {
            System.err.println(e + " " + wipe);
            LOGGER.log(Level.SEVERE, e + " " + wipe);
            return true;
          }
        } finally {
        }
      }
      // inPassword = cleanseInput(inPassword);
        
      loggedIn = true;
      Credentials loginCreds = new Credentials(inUsername, inPassword);  

      // creates a new customer controller object, also authenticates
      try {
        thisCust = new CustomerController(loginCreds);
      } catch (IOException ex) {
        System.out.println("Something went wrong with our database. Let's try again...");
        loggedIn = false;
      } catch (CustNotFoundException ex) {
        System.out.println("Username or password not found. Please try again.");
        loggedIn = false;
      }
    }
    
    if (wantsToQuit) {
      return true;
    } else {
      return false;
    }
  }



  // repeat menu until user wants to log out
  protected boolean showMenu() {

    Scanner scanner = new Scanner(System.in, "UTF-8");

    // show menu, capture user's choice, and call methods to carry out user actions
    System.out.println("1. Create a new account");
    System.out.println("2. Make a deposit");
    System.out.println("3. Make a withdrawal");
    System.out.println("4. Get your balance");
    System.out.println("5. Log out");
    System.out.print("What would you like to do? ");

    String token = scanner.next();

    if (token.compareTo("1") == 0) {
      newAccount();
    } else {
      if (token.compareTo("2") == 0) {
        makeDeposit();
      } else {
        if (token.compareTo("3") == 0) {
          makeWithdrawal();
        } else {
          if (token.compareTo("4") == 0) {
            getBalance();
          } else {
            if (token.compareTo("5") == 0) {
              logout();
              return true;
            }
          }
        }
      }
    }
    return false;
  }


  void newAccount() {
  
    Scanner scanner = new Scanner(System.in, "UTF-8");
    Float initialBalance = 0.0f;
    int accountNum;

    //first check to see if the customer already has an account
    if (thisCust.hasAcct) {
      System.out.println("You already have an account. You can only have one.");
      return;

    } else {

      // get the initial balance
      System.out.println("What initial balance are you depositing into your new account?");
      initialBalance = scanner.nextFloat();

      // authenticate the Admin
      thisAdmin = authenticateAdmin();

      // if the verification system says that the initial balance is right,
      // then create an AccountController object and let it create the new Account
      if (VS.confirmAmount(initialBalance)) {
        // first create the AccountController object, without an account number
        thisAcct = new AccountController(0);

        // then call newAccount, which creates a new account number
        accountNum = thisAcct.newAccount(initialBalance);

        // then link this new account to the current customer
        try {
          thisCust.linkAccount(accountNum, thisAcct);
        } catch (IOException ex) {
          System.out.println("Problem writing to database. Please try again.");
          return;
        } catch (CustNotFoundException ex) {
          System.out.println("Customer not found. Please try again.");
          return;
        }

        // if we get this far, then the account has been created successfully.
        System.out.println("Account created successfully.");

      } else {
        System.out.println("Amount not able to verified. Please try again.");
        return;
      }
    }
  }


  AdminController authenticateAdmin() {
      
    Scanner scanner = new Scanner(System.in, "UTF-8");

    // creates an AdminController object, gets credentials from user,
    // and then authenticates the object
      
    AdminController thisAdmin = new AdminController();
    System.out.println("Time to authenticate the Administrator. Please let the Administrator type in their credentials...");
    System.out.println("Admin's username: ");
    String username = scanner.nextLine();
    Console console = System.console();
    if (console == null) {
      System.out.println("console not active!");
      System.exit(0);
    }
    String password = new String(console.readPassword("Admin's password: "));
    try {
      username = cleanseInput(username);
      password = cleanseInput(password);
    } catch (MaliciousInputException e) {
      System.err.println(e + " " + wipe);
      LOGGER.log(Level.SEVERE, e + " " + wipe);
      System.exit(1);
    } finally {
    }
    //username = cleanseInput(username);
    //password = cleanseInput(password);
    Credentials loginCreds = new Credentials(username, password);

    if (thisAdmin.authenticate(loginCreds)) {
      return (thisAdmin);
    } else {
      System.out.println("Admin not verified.");
      return (null);
    }
  }


  void makeDeposit() {

    Scanner scanner = new Scanner(System.in, "UTF-8");
    Float depositAmount;
    boolean successful = false;

    // get the deposit amount 
    System.out.println("How much would you like to deposit? ");
    depositAmount = scanner.nextFloat();

    // ask customerController to make the deposit
    try {
      successful = thisCust.makeDeposit(depositAmount);
    } catch (CustomerNoAccountException ex) {
      System.out.println("You have to create an account before you can make a deposit.");
    } catch (AmountNotVerifiedException ex) {
      System.out.println("That amount could not be verified. Please try again.");
    } catch (CustomerNotAuthenticatedException ex) {
      System.out.println("Customer is not authenticated.");
    } catch (AccountNotFoundException ex) {
      System.out.println("Account not found.");
    } catch (IOException ex) {
      System.out.println("Sorry, we had a problem writing to the database. Please try again.");
    }

    if (successful) {
      System.out.println("Deposit successful. Thank you.");
    } else { 
      System.out.println("Deposit not successful. Our apologies");
    }
  }

  void makeWithdrawal() {
    
    Scanner scanner = new Scanner(System.in, "UTF-8");
    Float withdrawAmount;
    boolean successful = false;

    // get the withdrawal amount 
    System.out.println("How much would you like to withdraw?");
    withdrawAmount = scanner.nextFloat();

    // ask customerController to make the withdrawal
    try {
      successful = thisCust.makeWithdrawal(withdrawAmount);
    } catch (CustomerNoAccountException ex) {
      System.out.println("You have to create an account before you can make a deposit.");
    } catch (AmountNotVerifiedException ex) {
      System.out.println("That amount could not be verified. Please try again.");
    } catch (InsufficientFundsException ex) {
      System.out.println("Sorry, you don't have that much in your account.");
    } catch (CustomerNotAuthenticatedException ex) {
      System.out.println("Customer is not authenticated.");
    } catch (AccountNotFoundException ex) {
      System.out.println("Account not found.");
    } catch (IOException ex) {
      System.out.println("Sorry, we had a problem writing to the database. Please try again.");
    }

    if (successful) {
      System.out.println("Withdrawal successful. Thank you.");
    } else { 
      System.out.println("Withdrawal not successful. Our apologies");
    }
  }

  void getBalance() {

    Scanner scanner = new Scanner(System.in, "UTF-8");
    Float balance = 0.0f;

    try {
      balance = thisCust.getBalance();

    } catch (IOException ex) {
      System.out.println("Sorry, we had trouble reading from our database. Please try again.");
    } catch (CustomerNoAccountException ex) {
      System.out.println("You have to create an account before you can get the balance.");
    } catch (AccountNotFoundException ex) {
      System.out.println("Your account cannot be found.");
    }

    if (balance > 0) {
      System.out.println("Your balance is $" + Float.toString(balance));
    }
  }

  void logout() {
    Scanner scanner = new Scanner(System.in, "UTF-8");
    thisCust.logout();
    System.out.println("Goodbye.");
  }
}
