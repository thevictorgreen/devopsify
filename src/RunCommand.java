//RunCommand.java
package com.thevictorgreen;
import java.io.*;


public class RunCommand {

  public RunCommand() {

  }

  public static int exec(String command) {

    String s = null;
    int exitValue = 0;

    try {
      // Run command
      Process p = Runtime.getRuntime().exec(command);

      BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

      BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

      // Read the output from the command
      while ((s = stdInput.readLine()) != null) {
        System.out.println(s);
      }

      // Read any errors from the attempted command
      while ((s = stdError.readLine()) != null) {
        System.out.println(s);
      }

      p.waitFor();
      exitValue = p.exitValue();
    }
    catch (IOException | InterruptedException e) {
      System.out.println("Command Error: ");
      e.printStackTrace();
    }

    return exitValue;
  }
}
