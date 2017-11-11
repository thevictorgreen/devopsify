//Commit.java
package com.thevictorgreen;
import java.io.*;
import java.util.*;

public class Commit {

  private CloudApp cloudApp;
  private String serviceToCommit;

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

  //VALIDATION RULE
  //CHECK CURRENT DIRECTORY FOR .doac FOLDER
  //IF FOLDER EXISTS, ALREADY initialized
  //IF FOLDER DOES NOT EXIST, not initialized
  public boolean doacExists() {
    int exitValue = RunCommand.exec("ls .doac");
    boolean exists = exitValue == 0 ? true: false;
    return exists;
  }


  public Commit() {}


  public void pushIt(String msvcName, String repo) {
    //System.out.println(msvcName+" - "+repo);
    //RunCommand.exec("./plugins/gitpush.sh " + msvcName + " " + repo);

    StringBuilder sb = new StringBuilder();
    sb.append("cd "+msvcName+"\n");
    sb.append("git remote set-url origin "+repo+"\n");
    sb.append("git add -A\n");
    sb.append("git commit -m \"devops initial commit\"\n");
    sb.append("git push\n");

    if ( ExecuteBashScript.exec(sb) ) {
      System.out.println("SUCCESS");
    }
    else {
      System.out.println("FAILURE");
    }
  }

  public void pushItSeed(String msvcName, String repo) {
    //System.out.println(msvcName+" - "+repo);
    //RunCommand.exec("./plugins/gitpushseed.sh " + msvcName + " " + repo);

    StringBuilder sb = new StringBuilder();
    sb.append("cd "+msvcName+"\n");
    sb.append("git add -A\n");
    sb.append("git commit -m \"first commit\"\n");
    sb.append("git remote add origin "+repo+"\n");
    sb.append("git push -u origin master\n");

    if ( ExecuteBashScript.exec(sb) ) {
      System.out.println("SUCCESS");
    }
    else {
      System.out.println("FAILURE");
    }
  }


  public void saveSettings(String serviceToCommit) {

    Map<String, String>[] microservices = cloudApp.getMicroservices();
    for ( int i = 0;i < cloudApp.getMicroservices().length;i++ ) {
      if (cloudApp.getMicroservices()[i].get("status").equals("applied") && cloudApp.getMicroservices()[i].get("name").equals(serviceToCommit)) {
        microservices[i].replace("status","committed");
        break;
      }
    }
    this.cloudApp.setMicroservices( microservices );

    Map<String, String> seedsettings = this.cloudApp.getSeedsettings();
    if ( seedsettings.get("name").equals( serviceToCommit ) ) {
      seedsettings.replace("status","committed");
    }
    this.cloudApp.setSeedsettings( seedsettings );

    Map<String, String> appsettings = this.cloudApp.getAppsettings();
    appsettings.replace("lifecycle","committed");
    this.cloudApp.setAppsettings(appsettings);
    RunCommand.exec("rm doac.yaml");
    RunCommand.exec("rm .doac/state/doac.yaml");
    Config.saveSettings(cloudApp,"doac.yaml");
    Config.saveSettings(cloudApp,".doac/state/doac.yaml");
  }


  public void validate(String serviceToCommit) {

    boolean serviceMatch = false;
    boolean seedMatch = false;
    String repo = "";

    if (!doacExists()) {
      System.out.println(ANSI_RED + "NOT INITIALIZED" + ANSI_RESET);
      System.out.println(ANSI_RED + "Initialize by running the following command" + ANSI_RESET);
      System.out.println(ANSI_RED + "devopsify --init" + ANSI_RESET);
    }
    else {
      this.cloudApp = Config.getSettings(".doac/state/doac.yaml");
      if ( this.cloudApp.getAppsettings().get("lifecycle").equals("applied") || this.cloudApp.getAppsettings().get("lifecycle").equals("committed") ) {
        for ( int i = 0;i < cloudApp.getMicroservices().length;i++ ) {
          if ( cloudApp.getMicroservices()[i].get("name").equals( serviceToCommit ) ) {
            serviceMatch = true;
            repo = cloudApp.getMicroservices()[i].get("repourl");
            break;
          }
        }
        if ( serviceMatch ) {
          pushIt( serviceToCommit, repo );
          saveSettings( serviceToCommit );
        }
        else {
          if ( this.cloudApp.getSeedsettings().get("name").equals( serviceToCommit ) ) {
            repo = cloudApp.getSeedsettings().get("repourl");
            pushItSeed( serviceToCommit, repo );
            saveSettings( serviceToCommit );
          }
          else {
            System.out.println("NO MATCHING SERVICE OR SEED PROJECT FOUND");
          }
        }
      }
      else {
        System.out.println("NO APPLIED PLAN TO COMMIT");
      }
    }
  }
}
