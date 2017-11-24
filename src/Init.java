//Init.java
package com.thevictorgreen;
import java.io.*;
import java.util.*;

public class Init {

  // ANSI COLOR CODES FOR TERMINAL
  public final String ANSI_RESET = "\u001B[0m";
  public final String ANSI_BLACK = "\u001B[30m";
  public final String ANSI_RED = "\u001B[31m";
  public final String ANSI_GREEN = "\u001B[32m";
  public final String ANSI_YELLOW = "\u001B[33m";
  public final String ANSI_BLUE = "\u001B[34m";
  public final String ANSI_PURPLE = "\u001B[35m";
  public final String ANSI_CYAN = "\u001B[36m";
  public final String ANSI_WHITE = "\u001B[37m";

  public Init() {

  }

  //VALIDATION RULE
  //CHECK CURRENT DIRECTORY FOR .doac FOLDER
  //IF FOLDER EXISTS, ALREADY initialized
  //IF FOLDER DOES NOT EXIST, not initialized
  public boolean doacExists() {
    int exitValue = RunCommand.exec("ls .doac");
    boolean exists = exitValue == 0 ? true: false;
    return exists;
  }

  //Create .doac folder
  public boolean createDoacFolder() {
    int exitValue = RunCommand.exec("mkdir -p .doac/cache .doac/state");
    boolean status = exitValue == 0 ? true: false;
    return status;
  }

  //Create initial doac.yaml
  public boolean createDoacFile() {
    int exitValue = RunCommand.exec("touch doac.yaml");
    boolean status = exitValue == 0 ? true: false;
    return status;
  }

  //Populate doac.yaml
  public boolean populateDoacFile() {
    // root copy
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter("doac.yaml", true));

      writer.append("!<CloudApp>\n");
      writer.append("appsettings:\n");
      writer.append("  name: mycoolapp\n");
      writer.append("  sourcecoderepobase: https://github.com/username/\n");
      writer.append("  dockerhubuser: username/\n");
      writer.append("  provider: digitalocean\n");
      writer.append("  lifecycle: initialized\n");
      writer.append("\n");

      writer.append("microservices:\n");
      writer.append("  - s_id: 0\n");
      writer.append("    name: userportal\n");
      writer.append("    description: Application frontend for the user portal\n");
      writer.append("    tier: frontend\n");
      writer.append("    stack: angular4\n");
      writer.append("    jumpstart: yes\n");
      writer.append("    sourcerepo: devopsify-angular-simpliq\n");
      writer.append("    initcodebase: https://codeload.github.com/thevictorgreen/devopsify-angular-simpliq/zip/master\n");
      writer.append("    status:\n");
      writer.append("    repourl:\n");
      writer.append("\n");

      writer.append("seedsettings:\n");
      writer.append("  name:\n");
      writer.append("  status:\n");
      writer.append("  repourl:\n");

      writer.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    // cache copy
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(".doac/state/doac.yaml", true));

      writer.append("#CloudApp\n");
      writer.append("appsettings:\n");
      writer.append("  name: mycoolapp\n");
      writer.append("  sourcecoderepobase: https://github.com/username/\n");
      writer.append("  dockerhubuser: username/\n");
      writer.append("  provider: digitalocean\n");
      writer.append("  lifecycle: initialized\n");
      writer.append("\n");

      writer.append("microservices:\n");
      writer.append("  - s_id: 0\n");
      writer.append("    name: userportal\n");
      writer.append("    description: Application frontend for the user portal\n");
      writer.append("    tier: frontend\n");
      writer.append("    stack: angular4\n");
      writer.append("    jumpstart: yes\n");
      writer.append("    initcodebase: https://github.com/thevictorgreen/devopsify-angular-simpliq.git\n");
      writer.append("    status:\n");
      writer.append("    repourl:\n");
      writer.append("\n");

      writer.append("seedsettings:\n");
      writer.append("  name:\n");
      writer.append("  status:\n");
      writer.append("  repourl:\n");
      writer.append("\n");

      writer.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return true;
  }

  public void validate() {
    //System.out.println("INIT VALIDATION RULES");

    if ( !doacExists() ) {
      System.out.println(ANSI_GREEN + "INITIALIZING..." + ANSI_RESET);
      createDoacFolder();
      createDoacFile();
      populateDoacFile();
      System.out.println(ANSI_GREEN + "INITIALIZATION COMPLETE" + ANSI_RESET);
      System.out.println(ANSI_GREEN + "DEFINE YOUR APPLICATION BY EDITING doac.yaml" + ANSI_RESET);
      System.out.println(ANSI_GREEN + "SAVE CHANGES AND RUN THE FOLLOWING COMMAND" + ANSI_RESET);
      System.out.println(ANSI_GREEN + "devopsify --plan" + ANSI_RESET);
    }
    else {
      System.out.println(ANSI_BLUE + "PREVIOUSLY INITIALIZED" + ANSI_RESET);
      System.out.println(ANSI_GREEN + "DEFINE YOUR APPLICATION BY EDITING doac.yaml" + ANSI_RESET);
      System.out.println(ANSI_GREEN + "SAVE CHANGES AND RUN THE FOLLOWING COMMAND" + ANSI_RESET);
      System.out.println(ANSI_GREEN + "devopsify --plan" + ANSI_RESET);
    }

  }
}
