//Config.java
package com.thevictorgreen;
import java.io.*;
import java.util.*;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.yaml.snakeyaml.*;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.nodes.Tag;


public class Config {

  public Config() {

  }

  public static CloudApp getSettings(String fileName) {
    CloudApp cloudApp = null;

    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    try {
      cloudApp = mapper.readValue(new File(fileName), CloudApp.class);
    } catch(Exception e) {
       e.printStackTrace();
    }

    return cloudApp;
  }

  public static void saveSettings(CloudApp cloudApp, String fileName) {
    Representer representer = new Representer();
    representer.addClassTag(CloudApp.class, new Tag("CloudApp"));
    DumperOptions options = new DumperOptions();
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    options.setPrettyFlow(true);
    Yaml yaml = new Yaml(representer,options);

    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
      writer.write( yaml.dump(cloudApp) );
      writer.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

}
