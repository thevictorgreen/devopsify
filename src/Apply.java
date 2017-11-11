//Apply.java
package com.thevictorgreen;
import java.io.*;
import java.util.*;

public class Apply {

  private CloudApp cloudApp;
  private String appName;
  private String seedName;
  private String sourcecoderepobase;

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

  public Apply() {}

  //VALIDATION RULE
  //CHECK CURRENT DIRECTORY FOR .doac FOLDER
  //IF FOLDER EXISTS, ALREADY initialized
  //IF FOLDER DOES NOT EXIST, not initialized
  public boolean doacExists() {
    int exitValue = RunCommand.exec("ls .doac");
    boolean exists = exitValue == 0 ? true: false;
    return exists;
  }

  //Create folder
  public boolean createFolder(String name) {
    int exitValue = RunCommand.exec("mkdir " + name);
    boolean status = exitValue == 0 ? true: false;
    return status;
  }

  //git init
  public boolean gitInit(String folderName) {
    int exitValue = RunCommand.exec("git init " + folderName);
    boolean status = exitValue == 0 ? true: false;
    return status;
  }

  public void createSeedJob() {
    if ( this.cloudApp.getSeedsettings().get("status").equals("planned") ) {
      createFolder( this.seedName );
      gitInit( this.seedName );
      Map<String, String> seedsettings = this.cloudApp.getSeedsettings();
      seedsettings.replace("status","applied");
      this.cloudApp.setSeedsettings( seedsettings );
    }
  }

  public void createGroovyJobDSL() {
    if ( this.cloudApp.getAppsettings().get("lifecycle").equals("planned") ) {
      String[] microservices = new String[100];
      String basePath = this.appName.substring(0, 1).toUpperCase() + this.appName.substring(1) + "Project";

      String pre =
      "// Jenkins Job DSL to create Jobs\n" +
      "\n" +
      "//Base Path For All Jobs Related to this project\n" +
      "def basePath = '" + basePath + "';\n" +
      "\n" +
      "//Folder already exists. Seed job inside of it\n" +
      "folder (basePath) {\n" +
      "  displayName('" + basePath + "');\n" +
      "  description('Folder for " + basePath + "');\n" +
      "}\n" +
      "\n" +
      "\n"
      ;

      for (int i = 0;i < cloudApp.getMicroservices().length;i++) {

        String msvcName = cloudApp.getMicroservices()[i].get("name");
        String description = cloudApp.getMicroservices()[i].get("description");
        String ph = "";
        if (i == 0) {
          ph = "def ";
        }

        microservices[i] =
        "//Git repository for " + msvcName + "\n" +
        ph + "repoUrl = \"" + this.sourcecoderepobase + msvcName + ".git" + "\"; //Repository UrL\n" +
        "\n" +
        "pipelineJob(basePath + \"/" + msvcName + "\") { //JobName\n" +
        "  description(\"" + description + "\");\n" +
        "  definition {\n" +
        "    cpsScm {\n" +
        "      scriptPath(\"Jenkinsfile\"); //Path to Build Script\n" +
        "      scm {\n" +
        "        git {\n" +
        "          remote {\n" +
        "            url(repoUrl); //Git Repository\n" +
        "            branch(\"master\");\n" +
        "          }\n" +
        "        }\n" +
        "      }\n" +
        "      triggers {\n" +
        "        githubPush();\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "}\n" +
        "\n" +
        "\n"
        ;
      }

      String grooveMeBabyTonight = pre;
      for (int i = 0;i < cloudApp.getMicroservices().length;i++) {
        grooveMeBabyTonight += microservices[i];
      }

      //RunCommand.exec("touch " + this.seedName + "/ProjectSeedJob.groovy");

      try {
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.seedName + "/ProjectSeedJob.groovy", true));
        writer.write(grooveMeBabyTonight);
        writer.close();
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

      String markdown = "# Seed Project";

      try {
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.seedName + "/README.md", true));
        writer.write(markdown);
        writer.close();
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
  }

  //Create cache folder
  public boolean createCacheFolder(String name) {
    int exitValue = RunCommand.exec("mkdir .doac/cache/" + name);
    boolean status = exitValue == 0 ? true: false;
    return status;
  }

  public boolean downloadInitCodeBase(String initcodebase, String cache) {
    int exitValue = RunCommand.exec("git clone " + initcodebase + " .doac/cache/" + cache);
    boolean status = exitValue == 0 ? true: false;
    return status;
  }

  public void copyCacheIntoRepo(String cache) {
    //RunCommand.exec("./plugins/copycache.sh " + cache);
    StringBuilder sb = new StringBuilder();
    sb.append("cp .doac/cache/"+cache+"/.* "+cache+"\n");
    sb.append("cp -r .doac/cache/"+cache+"/ "+cache+"\n");

    if ( ExecuteBashScript.exec(sb) ) {
      System.out.println("SUCCESS");
    }
    else {
      System.out.println("FAILURE");
    }
  }

  public void pushIt(String msvcName, String repo) {
    RunCommand.exec("git -C " + msvcName + "/  status");
    System.out.println(repo);
  }

  public void createMicroservices() {
    Map<String, String>[] microservices = cloudApp.getMicroservices();
    for ( int i = 0;i < cloudApp.getMicroservices().length;i++ ) {
      if ( cloudApp.getMicroservices()[i].get("status").equals("planned") ) {
        createFolder( this.cloudApp.getMicroservices()[i].get("name") );
        gitInit( this.cloudApp.getMicroservices()[i].get("name") );
        createCacheFolder( this.cloudApp.getMicroservices()[i].get("name") );
        downloadInitCodeBase( this.cloudApp.getMicroservices()[i].get("initcodebase"), this.cloudApp.getMicroservices()[i].get("name") );
        copyCacheIntoRepo(this.cloudApp.getMicroservices()[i].get("name"));
        microservices[i].replace("status","applied");
      }
    }
    cloudApp.setMicroservices( microservices );
  }

  public void saveSettings() {
    Map<String, String> appsettings = this.cloudApp.getAppsettings();
    appsettings.replace("lifecycle","applied");
    this.cloudApp.setAppsettings(appsettings);
    RunCommand.exec("rm doac.yaml");
    RunCommand.exec("rm .doac/state/doac.yaml");
    Config.saveSettings(cloudApp,"doac.yaml");
    Config.saveSettings(cloudApp,".doac/state/doac.yaml");
  }


  public void showPlan() {
    this.cloudApp = Config.getSettings(".doac/state/doac.yaml");
    if ( this.cloudApp.getAppsettings().get("lifecycle").equals("applied") ) {
      System.out.println(ANSI_PURPLE+"The following resources have been created:"+ANSI_RESET);
      if ( cloudApp.getSeedsettings().get("status").equals("applied") ) {
        System.out.println("");
        System.out.println(ANSI_PURPLE+"    Seed Project:"+ANSI_RESET);
        System.out.println(ANSI_PURPLE+"      "+cloudApp.getSeedsettings().get("name")+""+ANSI_RESET);
        System.out.println(ANSI_YELLOW+"      (local repo) "+cloudApp.getSeedsettings().get("repourl")+""+ANSI_RESET);
        System.out.println("");
      }

      for (int i = 0;i < cloudApp.getMicroservices().length;i++) {
        if ( cloudApp.getMicroservices()[i].get("status").equals("applied") ) {
          System.out.println(ANSI_PURPLE+"    Microservices:"+ANSI_RESET);
          break;
        }
      }

      for (int i = 0;i < cloudApp.getMicroservices().length;i++) {
        if ( cloudApp.getMicroservices()[i].get("status").equals("applied") ) {
          System.out.println(ANSI_PURPLE+"      "+cloudApp.getMicroservices()[i].get("name")+""+ANSI_RESET);
          System.out.println(ANSI_YELLOW+"      (local repo) "+cloudApp.getMicroservices()[i].get("repourl")+""+ANSI_RESET);
          System.out.println("");
        }
      }
      System.out.println(ANSI_YELLOW+"Todo:"+ANSI_RESET);
      System.out.println(ANSI_YELLOW+"  Create matching non init remote repos then run"+ANSI_RESET);
      System.out.println(ANSI_YELLOW+"  devopsify --commit"+ANSI_RESET);
    }
    else {
      System.out.println("NO PLAN ON DECK. ADD A NEW MICROSERVICE TO doac.yaml");
    }
  }


  public void validate() {
    //System.out.println("Apply VALIDATION RULES");
    if (!doacExists()) {
      System.out.println(ANSI_RED + "NOT INITIALIZED" + ANSI_RESET);
      System.out.println(ANSI_RED + "Initialize by running the following command" + ANSI_RESET);
      System.out.println(ANSI_RED + "devopsify --init" + ANSI_RESET);
    }
    else {
      this.cloudApp = Config.getSettings(".doac/state/doac.yaml");
      this.appName = this.cloudApp.getAppsettings().get("name");
      this.seedName = this.cloudApp.getAppsettings().get("name") + "-seed";
      this.sourcecoderepobase = this.cloudApp.getAppsettings().get("sourcecoderepobase");

      if ( this.cloudApp.getAppsettings().get("lifecycle").equals("planned") ) {
        createSeedJob();
        createGroovyJobDSL();
        createMicroservices();
        saveSettings();
        showPlan();
      }
      else {
        System.out.println("NO PLAN TO APPLY");
      }
    }
  }

}
