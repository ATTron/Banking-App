package swsec.bank.models;

public class Customer {
    
  // Instance variables
  private String username;  //used as a unique identifier
  private String password;
  private int accountNum;
  private String name;
  private String address;
  private boolean authenticated = false;

  
  public Customer(String username, String password, String name, String address) {
    // Use when all attributes are known
    this.username = username;
    this.password = password;
    this.name = name;
    this.address = address;
  }

  public Customer (String username, String password) {
    //sometimes a Customer object needs to be created in order to retrieve Customer attributes
    this.username = username;
    this.password = password;
  }

  public void markAuthenticated () {
    // called after the associated Repository object confirms credentials
    this.authenticated = true;
  }

  public void setAccountNum (int accountNum) {
    this.accountNum = accountNum;
  }

  public void setName (String name) {
    this.name = name;
  }

  public void setAddress (String address) {
    this.address = address;
  }

  public void setCredentials (String username, String password) {
    this.username = username; 
    this.password = password;
  }

  public void logout () {
    this.authenticated = false;
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

  public String getAddress () {
    return this.address;
  }

  public int getAccountNum () {
    return this.accountNum;
  }

  public boolean isAuthenticated () {
    return this.authenticated;
  }

}
