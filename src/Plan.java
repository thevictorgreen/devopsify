//Plan.java
package com.thevictorgreen;
import java.io.*;
import java.util.*;

public class Plan {

  private CloudApp cloudApp;

  public Plan() {

  }

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


  public void createSeedPlan() {
    Map<String,String> seedsettings_map = this.cloudApp.getSeedsettings();
    Map<String, String> appsettings = this.cloudApp.getAppsettings();
    if ( this.cloudApp.getSeedsettings().get("status") == null ) {
      seedsettings_map.replace("name",this.cloudApp.getAppsettings().get("name")+"-seed");
      seedsettings_map.replace("status","planned");
      seedsettings_map.replace("repourl",this.cloudApp.getAppsettings().get("sourcecoderepobase")+this.cloudApp.getAppsettings().get("name")+"-seed.git");
      appsettings.replace("lifecycle","planned");
    }
    this.cloudApp.setAppsettings(appsettings);
    this.cloudApp.setSeedsettings( seedsettings_map );
  }

  public void createMicroservicesPlan() {
    Map<String, String> appsettings = this.cloudApp.getAppsettings();
    Map<String, String>[] microservices = cloudApp.getMicroservices();
    for ( int i = 0; i < cloudApp.getMicroservices().length; i++ ) {
      if ( cloudApp.getMicroservices()[i].get("status") == null ) {
        microservices[i].replace("name",cloudApp.getAppsettings().get("name") + "-" + cloudApp.getMicroservices()[i].get("name"));
        microservices[i].replace("status","planned");
        microservices[i].replace("repourl",cloudApp.getAppsettings().get("sourcecoderepobase") + cloudApp.getMicroservices()[i].get("name") + ".git");
        appsettings.replace("lifecycle","planned");
      }
    }
    cloudApp.setMicroservices( microservices );
    this.cloudApp.setAppsettings(appsettings);
  }

  public void saveSettings() {
    RunCommand.exec("rm doac.yaml");
    RunCommand.exec("rm .doac/state/doac.yaml");
    Config.saveSettings(cloudApp,"doac.yaml");
    Config.saveSettings(cloudApp,".doac/state/doac.yaml");
  }


  public void showPlan() {
    this.cloudApp = Config.getSettings(".doac/state/doac.yaml");
    if ( this.cloudApp.getAppsettings().get("lifecycle").equals("planned") ) {
      System.out.println(ANSI_PURPLE+"The following resources will be created on --apply:"+ANSI_RESET);
      if ( cloudApp.getSeedsettings().get("status").equals("planned") ) {
        System.out.println("");
        System.out.println(ANSI_PURPLE+"    Seed Project:"+ANSI_RESET);
        System.out.println(ANSI_PURPLE+"      "+cloudApp.getSeedsettings().get("name")+""+ANSI_RESET);
        System.out.println(ANSI_YELLOW+"      (local repo) "+cloudApp.getSeedsettings().get("repourl")+""+ANSI_RESET);
        System.out.println("");
      }

      for (int i = 0;i < cloudApp.getMicroservices().length;i++) {
        if ( cloudApp.getMicroservices()[i].get("status").equals("planned") ) {
          System.out.println(ANSI_PURPLE+"    Microservices:"+ANSI_RESET);
          break;
        }
      }

      for (int i = 0;i < cloudApp.getMicroservices().length;i++) {
        if ( cloudApp.getMicroservices()[i].get("status").equals("planned") ) {
          System.out.println(ANSI_PURPLE+"      "+cloudApp.getMicroservices()[i].get("name")+""+ANSI_RESET);
          System.out.println(ANSI_YELLOW+"      (local repo) "+cloudApp.getMicroservices()[i].get("repourl")+""+ANSI_RESET);
          System.out.println("");
        }
      }
      System.out.println(ANSI_YELLOW+"Todo:"+ANSI_RESET);
      System.out.println(ANSI_YELLOW+"  devopsify --apply"+ANSI_RESET);
    }
    else {
      System.out.println("NO PLAN ON DECK. ADD A NEW MICROSERVICE TO doac.yaml");
    }
  }


  public void validate() {
    if ( !doacExists() ) {
      System.out.println(ANSI_RED + "NOT INITIALIZED" + ANSI_RESET);
      System.out.println(ANSI_RED + "Initialize by running the following command" + ANSI_RESET);
      System.out.println(ANSI_RED + "devopsify --init" + ANSI_RESET);
    }
    else {
      System.out.println(ANSI_GREEN + "PREPARING PLAN" + ANSI_RESET);
      this.cloudApp = Config.getSettings("doac.yaml");
      createSeedPlan();
      createMicroservicesPlan();
      saveSettings();
      showPlan();
    }
  }

}
