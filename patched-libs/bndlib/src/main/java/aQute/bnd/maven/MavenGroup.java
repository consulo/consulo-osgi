package aQute.bnd.maven;

import aQute.bnd.service.Plugin;
import aQute.libg.reporter.Reporter;

import java.util.Map;

public class MavenGroup implements BsnToMavenPath, Plugin {
  String groupId = "";

  public String[] getGroupAndArtifact(String bsn) {
    String[] result = new String[2];
    result[0] = groupId;
    result[1] = bsn;
    return result;
  }

  public void setProperties(Map<String, String> map) {
    if (map.containsKey("groupId")) {
      groupId = map.get("groupId");
    }
  }

  public void setReporter(Reporter processor) {
  }

}
