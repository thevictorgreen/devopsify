//CloudApp.java
package com.thevictorgreen;
import java.io.*;
import java.util.*;


public class CloudApp {

  private Map<String, String> appsettings;

  public Map<String, String> getAppsettings() {
    return this.appsettings;
  }

  public void setAppsettings(Map<String, String> appsettings) {
    this.appsettings = appsettings;
  }

  private Map<String, String>[] microservices;

  public Map<String, String>[] getMicroservices() {
    return this.microservices;
  }

  public void setMicroservices(Map<String, String>[] microservices) {
    this.microservices = microservices;
  }

  private Map<String, String> seedsettings;

  public Map<String, String> getSeedsettings() {
    return this.seedsettings;
  }

  public void setSeedsettings(Map<String, String> seedsettings) {
    this.seedsettings = seedsettings;
  }

  public CloudApp() {

  }

}
