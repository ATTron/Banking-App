package swsec.bank.services;

import java.util.Random;

/**
 * Created by cseaman on 3/8/2016.
 * This class simulates a physical system (e.g. an ATM or a teller) 
 * that can physically verify
 * that money has changed hands. 
 * In a real scenario, the verification system would communicate with the
 * rest of the application over a network, 
 * but here we are simulating it with a simple method call.
 */
public class verificationSystem {

  Random rand;

  public verificationSystem() {
    rand = new Random();
  }




  public boolean confirmAmount(float amount) {
    return (rand.nextInt(9) > 0);   // this will return true 90% of the time
  }
}
