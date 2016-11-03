package swsec.bank.services;

import org.junit.Assert;
import org.junit.Test;
import swsec.bank.controllers.*;
import swsec.bank.services.UiService;
import swsec.bank.services.Credentials;

import swsec.bank.exceptions.MaliciousInputException;

public class TestUiService{
  @Test
  public void testWithdrawlDeposit(){
    try{
      Credentials creds = new Credentials("biscuit", "cseaman");
      CustomerController thisCust = new CustomerController(creds);
      float initalBalance = thisCust.getBalance();
      boolean successfulWD = thisCust.makeWithdrawal((float)200);
      boolean successfulDep = thisCust.makeDeposit((float)150);
      Assert.assertEquals((float)initalBalance-200+150, thisCust.getBalance(), 0);
    }
    catch(Exception e){
    }
    finally{
    }
  }

  @Test
  public void testCleanseInput(){
    UiService UiS = new UiService();
    boolean thrown = false;

    try{
      UiS.cleanseInput("<h1>cseama$$$$$$n--+==#$%^&*()(*&^%$#$%^&*");
    }
    catch(MaliciousInputException e){
      thrown = true;
    }
    finally{
    }
    Assert.assertTrue(thrown);
  }
}
