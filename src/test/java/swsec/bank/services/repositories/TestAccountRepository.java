package swsec.bank.services.repositories;

import org.junit.Assert;
import org.junit.Test;
import swsec.bank.models.Account;
import swsec.bank.services.repositories.AccountRepository;

/**
 *  * Created by cseaman on 4/4/2016.
 *   */
public class TestAccountRepository{
  @Test
  public void testModifyBalance(){
    try{
      // create new account with balance of $100
      Account acct = new Account (1234, (float)100);

      // create repository instance that can change balance
      AccountRepository acctRep = new AccountRepository();

      // modify the balance
      acctRep.modifyBalance(acct, (float)200);

      //test that the new balance is actually $200
      Assert.assertEquals ((float)200, acctRep.getBalance (1234), 0);
    }
    catch(Exception e){
    }
    finally{
    }
  }
}
