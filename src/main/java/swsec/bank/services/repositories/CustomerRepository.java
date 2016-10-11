package swsec.bank.services.repositories;

import swsec.bank.models.Customer;
import swsec.bank.exceptions.CustNotFoundException;
import java.io.*;


/**
 * This class reads from and writes to the customers.txt file which stores all the
 * customers in the system.
 */
public class CustomerRepository {
  
  private static final String CUSTOMER_FILE = "customers.txt";   
  
  public CustomerRepository() {
  
  }
  
  
  public void store (Customer thisCust) {
    // write the customer's instance variables to the text file
    // Don't need this for now - we will assume the text file is pre-populated with all existing customers
  }

  public void update (Customer thisCust) throws IOException, CustNotFoundException {
    // overwrite the information for an existing customer

    try {

      BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (CUSTOMER_FILE), "UTF-8"));

          try {
            String line = "";
            String text = "";  //the entire contents of the customer file

            while ((line = br.readLine ()) != null) {

              if (line.compareTo(thisCust.getUsername()) == 0) { //add all the new instance variables to text
                text = text.concat (line);
                text = text.concat ("\n");
                text = text.concat (thisCust.getPassword());
                text = text.concat ("\n");
                text = text.concat (Integer.toString (thisCust.getAccountNum()));
                text = text.concat ("\n");
                text = text.concat (thisCust.getName());
                text = text.concat ("\n");
                text = text.concat (thisCust.getAddress());
                text = text.concat ("\n");
                br.readLine ();  //skip over the old values of the instance variables
                br.readLine ();
                br.readLine ();
                br.readLine ();
              } else {
                text = text.concat (line);
                text = text.concat ("\n");
              }
            }

            if (text.indexOf (thisCust.getUsername()) == -1) {
              throw new CustNotFoundException (thisCust.getUsername());
            }

            try {
              OutputStreamWriter os = new OutputStreamWriter (new FileOutputStream (CUSTOMER_FILE), "UTF-8");
              try {
                os.write (text);  //rewrite the entire file with the "text" string
              } finally {
                os.close ();
              }
            } catch (IOException e) {
              System.out.println ("could not write to file");
            }
          } finally {
            br.close();
          }

        } catch (IOException e) {
          System.out.println ("File could not be read/found.");
        }
    }


  public Customer lookup (Customer thisCust) throws IOException {
    // use thisCust.username to look up the customer in the text file
    // if found, check stored password against thisCust.password
    // if matched, mark returned Customer object as authenticated and load instance variables
    // assumes instance variables are stored on separate lines in the following order: 
    // username, password, account number, name, address, blank line

    Customer tempCust = thisCust;  // initialize the temp variable

    try {

      BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (CUSTOMER_FILE), "UTF-8"));

      try {

        String line = "";
        String lastLine = "";

        while ((line = br.readLine()) != null) {

          if (line.indexOf (thisCust.getUsername()) != -1 && lastLine.length() == 0) {
            String pass = br.readLine ();

            if (pass.compareTo(thisCust.getPassword()) == 0) {
              tempCust.markAuthenticated ();
              tempCust.setAccountNum (Integer.parseInt(br.readLine ()));
              tempCust.setName (br.readLine ());
              tempCust.setAddress (br.readLine ());
            } else {
              tempCust.setAccountNum (-1);
            }
          }

          lastLine = line;

        }
      } finally {
        br.close ();
      }

    } catch (IOException e) {
        System.out.println("File could not be read/found.");
    }
    return (tempCust);
  }

}
