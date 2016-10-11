package swsec.bank;

import swsec.bank.services.UiService;

/** 
 *  This class starts the program and should not be modified.
 */
public class Runner {

  public static void main(String[] args) {
    Runnable start = new UiService();

    start.run();
  }
}
