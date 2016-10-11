package swsec.bank.services.repositories;

import swsec.bank.models.Admin;
import java.io.*;

/**
 * This class reads from and writes to the admin.txt file which stores all the
 * admins in the system.
 */

public class AdminRepository {
  
private static final String ADMIN_FILE = "admins.txt";

  public AdminRepository() {  //constructor really doesn't do anything
  
  }

  
  // public Admin store(Admin thisAdmin) {
    // write all thisAdmin's instance variables to the text file
    // Don't need this for now - we will assume the text file is pre-populated with all existing admins
  // }


  public Admin lookup (Admin thisAdmin) {
    // use thisAdmin.username to look up the Admin in the text file
    // if found, check stored password against thisAdmin.password
    // if matched, mark returned Admin object as authenticated and load instance variables
    // assumes instance variables are stored on separate lines in the following order: 
    // username, password, name, employee id, blank line

    Admin tempAdmin = new Admin (thisAdmin.getUsername(), thisAdmin.getPassword (), thisAdmin.getName (), thisAdmin.getEmployeeId ());

    try {

      BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (ADMIN_FILE), "UTF-8"));
      try {

        String line = "";
        String lastLine = "";

        while ((line = br.readLine()) != null) {

          if (line.indexOf (thisAdmin.getUsername()) != -1 && lastLine.length() == 0) {
            String pass = br.readLine ();
            
            if (pass.compareTo(thisAdmin.getPassword ()) == 0) {
              tempAdmin.markAuthenticated ();
            }

          }
          lastLine = line;

        }
      } finally {
        br.close ();
        return (tempAdmin);
      }

    } catch (IOException e) {
        System.out.println("File could not be read/found.");
        return (tempAdmin);
    }
  }

}
