package swsec.bank.services.repositories;

import swsec.bank.exceptions.AccountNotFoundException;
import swsec.bank.models.Account;

import java.io.*;


/**
 * This class reads from and writes to the accounts.txt file which stores all the
 * accounts in the system.
 */

public class AccountRepository {
  
  private static final String ACCOUNT_FILE = "accounts.txt";
 
  public AccountRepository() {  //constructor really doesn't do anything
   
  }

  public void save(Account thisAcct) {
    // write all thisAcct's instance variables to the text file

    try {

      BufferedReader br = new BufferedReader(new InputStreamReader(
            new FileInputStream(ACCOUNT_FILE), "UTF-8"));

      try {
        String line = "";
        String text = ""; // the entire contents of the accounts file

        // copy all account information into text string
        while ((line = br.readLine()) != null) {
          text = text.concat(line);
          text = text.concat("\n");
        }

        // add new account to end of text string
        text = text.concat(Integer.toString(thisAcct.getAccountNum()));
        text = text.concat("\n");
        text = text.concat(Float.toString(thisAcct.getCurrentBalance()));
        text = text.concat("\n");

        try {
          OutputStreamWriter os = new OutputStreamWriter(
              new FileOutputStream(ACCOUNT_FILE), "UTF-8");
          try {
            os.write(text);  //rewrite the entire file with the "text" string
          } finally {
            os.close();
          }
        } catch (IOException e) {
          System.out.println("could not write to file");
        }
      } finally {
        br.close();
      }

    } catch (IOException e) {
      System.out.println("File could not be read/found");
    }
  }


  public void modifyBalance(Account thisAcct, float newBalance) 
    throws IOException, AccountNotFoundException {

    int accountNum = thisAcct.getAccountNum();

    // look up thisAcct in the text file and change the associated balance to newBalance

    try {

      BufferedReader br = new BufferedReader(new InputStreamReader(
            new FileInputStream(ACCOUNT_FILE), "UTF-8"));

      try {
        String line = "";
        String text = ""; // the entire contents of the accounts file

        // copy all account information into text string
        while ((line = br.readLine()) != null) {

          if (line.compareTo(Integer.toString(accountNum)) == 0) {

            text = text.concat(line);
            text = text.concat("\n");
            text = text.concat(Float.toString(newBalance));
            text = text.concat("\n");
            line = br.readLine();   //skip next line, which contains old balance
          } else {
            text = text.concat(line);
            text = text.concat("\n");
          }
        }

        if (text.indexOf(Integer.toString(accountNum)) == -1) {

          throw new AccountNotFoundException(accountNum);
        }

        try {
          OutputStreamWriter os = new OutputStreamWriter(
              new FileOutputStream(ACCOUNT_FILE), "UTF-8");
          try {
            os.write(text);   //rewrite the entire file with the "text" string
          } finally {
            os.close();
          }
        } catch (IOException e) {
          System.out.println("could not write to file");
        }
      } finally {
        br.close();
      }

    } catch (IOException e) {
      System.out.println("File could not be read/found.");
    }
  }
                        


  public float getBalance(int accountNum) throws IOException {
    // look up thisAcct in the text file and return the associated balance

    BufferedReader br;
    float balance = 0;

    try {
      br = new BufferedReader(new InputStreamReader(
            new FileInputStream(ACCOUNT_FILE), "UTF-8"));

      String strAccountNum = Integer.toString(accountNum);

      while (strAccountNum.compareTo(br.readLine()) != 0) {}

      balance = Float.parseFloat(br.readLine());
        
    } catch (IOException e) {
      System.out.println("File could not be read/found.");
    } finally {
      return (balance);
    }
  }
}
