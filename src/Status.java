//Status.java
package com.thevictorgreen;
import java.io.*;
import java.util.*;

public class Status {

  private CloudApp cloudApp;

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

  public Status() {}

  //VALIDATION RULE
  //CHECK CURRENT DIRECTORY FOR .doac FOLDER
  //IF FOLDER EXISTS, ALREADY initialized
  //IF FOLDER DOES NOT EXIST, not initialized
  public boolean doacExists() {
    int exitValue = RunCommand.exec("ls .doac");
    boolean exists = exitValue == 0 ? true: false;
    return exists;
  }

  public void generateReport() {
    this.cloudApp = Config.getSettings(".doac/state/doac.yaml");

    // SEED JOB STATUS
    if ( cloudApp.getSeedsettings().get("status") == null ) {
      System.out.println("");
      System.out.println(ANSI_PURPLE+"The following resources have been created:"+ANSI_RESET);
      System.out.println(ANSI_CYAN+"For ["+this.cloudApp.getAppsettings().get("name")+"]"+ANSI_RESET);
      System.out.println("");
      System.out.println(ANSI_PURPLE+"    Seed Project:"+ANSI_RESET);
      System.out.println(ANSI_BLUE+"      todo: devopsify --plan"+ANSI_RESET);
    }
    else if ( cloudApp.getSeedsettings().get("status").equals("planned") ) {
      System.out.println("");
      System.out.println(ANSI_PURPLE+"The following resources have been created:"+ANSI_RESET);
      System.out.println(ANSI_CYAN+"For ["+this.cloudApp.getAppsettings().get("name")+"]"+ANSI_RESET);
      System.out.println("");
      System.out.println(ANSI_PURPLE+"    Seed Project:"+ANSI_RESET);
      System.out.println(ANSI_PURPLE+"      "+cloudApp.getSeedsettings().get("name")+""+ANSI_RESET);
      System.out.println(ANSI_RED+"      (local repo) "+cloudApp.getSeedsettings().get("repourl")+""+ANSI_RESET);
      System.out.println(ANSI_RED+"      todo: devopsify --apply"+ANSI_RESET);
    }
    else if ( cloudApp.getSeedsettings().get("status").equals("applied") ) {
      System.out.println("");
      System.out.println(ANSI_PURPLE+"The following resources have been created:"+ANSI_RESET);
      System.out.println(ANSI_CYAN+"For ["+this.cloudApp.getAppsettings().get("name")+"]"+ANSI_RESET);
      System.out.println("");
      System.out.println(ANSI_PURPLE+"    Seed Project:"+ANSI_RESET);
      System.out.println(ANSI_PURPLE+"      "+cloudApp.getSeedsettings().get("name")+""+ANSI_RESET);
      System.out.println(ANSI_YELLOW+"      (local repo) "+cloudApp.getSeedsettings().get("repourl")+""+ANSI_RESET);
      System.out.println(ANSI_YELLOW+"      todo: devopsify --commit "+cloudApp.getSeedsettings().get("name")+""+ANSI_RESET);
    }
    else if ( cloudApp.getSeedsettings().get("status").equals("committed") ) {
      System.out.println("");
      System.out.println(ANSI_PURPLE+"The following resources have been created:"+ANSI_RESET);
      System.out.println(ANSI_CYAN+"For ["+this.cloudApp.getAppsettings().get("name")+"]"+ANSI_RESET);
      System.out.println("");
      System.out.println(ANSI_PURPLE+"    Seed Project:"+ANSI_RESET);
      System.out.println(ANSI_PURPLE+"      "+cloudApp.getSeedsettings().get("name")+""+ANSI_RESET);
      System.out.println(ANSI_GREEN+"      (remote repo) "+cloudApp.getSeedsettings().get("repourl")+""+ANSI_RESET);
    }

    // MICROSERVICES STATUS
    for (int i = 0;i < cloudApp.getMicroservices().length;i++) {
      if ( cloudApp.getMicroservices()[i].get("status") == null ) {
        System.out.println("");
        System.out.println(ANSI_PURPLE+"    Microservice:"+ANSI_RESET);
        System.out.println(ANSI_BLUE+"      todo: devopsify --plan"+ANSI_RESET);
      }
      else if ( cloudApp.getMicroservices()[i].get("status").equals("planned") ) {
        System.out.println("");
        System.out.println(ANSI_PURPLE+"    Microservice:"+ANSI_RESET);
        System.out.println(ANSI_PURPLE+"      "+cloudApp.getMicroservices()[i].get("name")+""+ANSI_RESET);
        System.out.println(ANSI_RED+"      (local repo) "+cloudApp.getMicroservices()[i].get("repourl")+""+ANSI_RESET);
        System.out.println(ANSI_RED+"      todo: devopsify --apply"+ANSI_RESET);
      }
      else if ( cloudApp.getMicroservices()[i].get("status").equals("applied") ) {
        System.out.println("");
        System.out.println(ANSI_PURPLE+"    Microservice:"+ANSI_RESET);
        System.out.println(ANSI_PURPLE+"      "+cloudApp.getMicroservices()[i].get("name")+""+ANSI_RESET);
        System.out.println(ANSI_YELLOW+"      (local repo) "+cloudApp.getMicroservices()[i].get("repourl")+""+ANSI_RESET);
        System.out.println(ANSI_YELLOW+"      todo: devopsify --commit "+cloudApp.getMicroservices()[i].get("name")+""+ANSI_RESET);
      }
      else if ( cloudApp.getMicroservices()[i].get("status").equals("committed") ) {
        System.out.println("");
        System.out.println(ANSI_PURPLE+"    Microservice:"+ANSI_RESET);
        System.out.println(ANSI_PURPLE+"      "+cloudApp.getMicroservices()[i].get("name")+""+ANSI_RESET);
        System.out.println(ANSI_GREEN+"      (remote repo) "+cloudApp.getMicroservices()[i].get("repourl")+""+ANSI_RESET);
      }
    }
    System.out.println("");
  }

  public void validate() {
    //System.out.println("Apply VALIDATION RULES");
    if (!doacExists()) {
      System.out.println(ANSI_RED + "NOT INITIALIZED" + ANSI_RESET);
      System.out.println(ANSI_RED + "Initialize by running the following command" + ANSI_RESET);
      System.out.println(ANSI_RED + "devopsify --init" + ANSI_RESET);
    }
    else {
      generateReport();
    }
  }
}
