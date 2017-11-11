//Devopsify.java
package com.thevictorgreen;
import java.io.*;
import java.util.*;


public class Devopsify {


  // ANSI COLOR CODES FOR TERMINAL
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_BLACK = "\u001B[30m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_YELLOW = "\u001B[33m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_PURPLE = "\u001B[35m";
  public static final String ANSI_CYAN = "\u001B[36m";
  public static final String ANSI_WHITE = "\u001B[37m";


  // PROGRAM ENTRYPOINT
  public static void main(String[] args) {

    // CHECK FOR COMMAND LINE ARGUMENTS

    // GENERATE HELP DOCS
    if ( args.length == 0 ) {
      System.out.println(ANSI_RED + "Usage:" + ANSI_RESET);
      System.out.println("devopsify -i");
      System.out.println("devopsify --init");
      System.out.println(" Must be run first");
      System.out.println(" Initializes project folder");
      System.out.println("");

      System.out.println("devopsify -p");
      System.out.println("devopsify --plan");
      System.out.println(" Creates the devopsified plan for your App");
      System.out.println("");

      System.out.println("devopsify -a");
      System.out.println("devopsify --apply");
      System.out.println(" Applies the devopsified plan for your App locally");
      System.out.println("");

      System.out.println("devopsify -c microservice-name");
      System.out.println("devopsify --commit microservice-name");
      System.out.println(" Performs initial service push to remote repository");
      System.out.println(" git remote add origin https://github.com/username/new_repo");
      System.out.println(" git push -u origin master");
      System.out.println("");

      System.out.println("devopsify -s");
      System.out.println("devopsify --status");
      System.out.println(" View Status of this Apps Devopsification");
      System.out.println("");

      System.exit(0);
    }

    // INIT
    else if ( (args.length == 1) &&  (args[0].equals("-i")) || (args.length == 1) && (args[0].equals("--init")) ) {
      Init init = new Init();
      init.validate();
    }

    // PLAN
    else if ( (args.length == 1) &&  (args[0].equals("-p")) || (args.length == 1) && (args[0].equals("--plan")) ) {
      Plan plan = new Plan();
      plan.validate();
    }

    // APPLY
    else if ( (args.length == 1) &&  (args[0].equals("-a")) || (args.length == 1) && (args[0].equals("--apply")) ) {
      Apply apply = new Apply();
      apply.validate();
    }

    // COMMIT
    else if ( (args.length == 2) &&  (args[0].equals("-c")) || (args.length == 2) && (args[0].equals("--commit")) ) {
      Commit commit = new Commit();
      commit.validate(args[1]);
    }

    // STATUS
    else if ( (args.length == 1) &&  (args[0].equals("-s")) || (args.length == 1) && (args[0].equals("--status")) ) {
      Status status = new Status();
      status.validate();
    }

    // EXPERIMENTAL
    else if ( (args.length == 1) &&  (args[0].equals("-x")) || (args.length == 1) && (args[0].equals("--xpm")) ) {
      Xpm xpm = new Xpm();
      xpm.validate();
    }

    // GENERATE HELP DOCS
    else {
      System.out.println(ANSI_RED + "Usage:" + ANSI_RESET);
      System.out.println("devopsify -i");
      System.out.println("devopsify --init");
      System.out.println(" Must be run first");
      System.out.println(" Initializes project folder");
      System.out.println("");

      System.out.println("devopsify -p");
      System.out.println("devopsify --plan");
      System.out.println(" Creates the devopsified plan for your App");
      System.out.println("");

      System.out.println("devopsify -a");
      System.out.println("devopsify --apply");
      System.out.println(" Applies the devopsified plan for your App locally");
      System.out.println("");

      System.out.println("devopsify -c microservice-name");
      System.out.println("devopsify --commit microservice-name");
      System.out.println(" Performs initial service push to remote repository");
      System.out.println(" git remote add origin https://github.com/username/new_repo");
      System.out.println(" git push -u origin master");
      System.out.println("");

      System.out.println("devopsify -s");
      System.out.println("devopsify --status");
      System.out.println(" View Status of this Apps Devopsification");
      System.out.println("");

      System.exit(0);
    }


  }

}
