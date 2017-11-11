//ExecuteBashScript.java
package com.thevictorgreen;
import java.io.*;

public class ExecuteBashScript {

  public ExecuteBashScript() {

  }

  public static boolean exec(StringBuilder sb) {

    boolean result = false;

    try {
      // Start the shell
      ProcessBuilder pb = new ProcessBuilder("/bin/bash");
      Process bash = pb.start();

      // Pass commands to the shell
      PrintStream ps = new PrintStream(bash.getOutputStream());
      ps.println(sb);
      ps.close();

      // Get an InputStream for the stdout of the shell
      BufferedReader br = new BufferedReader( new InputStreamReader(bash.getInputStream()));

      // Retrieve and print output
      String line;
      while (null != (line = br.readLine())) {
        System.out.println("> "+line);
      }
      br.close();

      // Make sure the shell has terminated, print out exit value
      int exitValue =  bash.waitFor();
      result = exitValue == 0 ? true: false;
      
    } catch(Exception e) {
      e.printStackTrace();
    }
    return result;
  }
}
