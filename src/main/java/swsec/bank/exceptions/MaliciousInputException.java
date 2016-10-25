package swsec.bank.exceptions;

public class MaliciousInputException extends Exception {

  private String loginCreds;
  private boolean isUser;
  private String errorUser = "Entered invalid characters. Closing application...";
  private String errorDev = "The user has entered in invalid characters for login: ";

  public MaliciousInputException(){
    super ();
  }

  public String getLoginCreds(){
    return loginCreds;
  }
  
  public boolean getInfo(){
    return isUser;
  }

  public String toStringUser(){
    return errorUser;
  }

  public String toString(){
    return errorDev;
  }
}
