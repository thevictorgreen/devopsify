//Xpm.java
package com.thevictorgreen;
import java.io.*;
import java.util.*;

public class Xpm {

  public Xpm() {}

  public void validate() {

    StringBuilder sb = new StringBuilder();
    sb.append("echo $PATH\n");
    sb.append("cd vibe-userportal\n");
    sb.append("git status\n");

    if ( ExecuteBashScript.exec(sb) ) {
      System.out.println("SUCCESS");
    }
    else {
      System.out.println("FAILURE");
    }

  }
}
