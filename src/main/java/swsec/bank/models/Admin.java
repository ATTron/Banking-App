package swsec.bank.models;

public class Admin {
  
  // Instance variables
  private String username;
  private String password;
  private String name;
  private String employeeId;
  private boolean authenticated = false;


  public Admin() {

    // needs to pass in values for all instance variables and assign those values to the variables
  }

  public Admin(String username, String password, String name, String employeeId) {
    // Use when all attributes are known
    this.username = username;
    this.password = password;
    this.name = name;
    this.employeeId = employeeId;
  }

  public Admin (String name, String employeeId) {
    //sometimes an Admin object needs to be created before login credentials are known
    this.name = name;
    this.employeeId = employeeId;
  }

  public void setCredentials (String username, String password) {
    this.username = username;
    this.password = password;
  }

  public void markAuthenticated () {
    this.authenticated = true;
  }

  public String getUsername () {
    return this.username;
  }

  public String getPassword () {
    return this.password;
  }

  public String getName () {
    return this.name;
  }

  public String getEmployeeId () {
    return this.employeeId;
  }

  public boolean isAuthenticated () {
    return this.authenticated;
  }

}
