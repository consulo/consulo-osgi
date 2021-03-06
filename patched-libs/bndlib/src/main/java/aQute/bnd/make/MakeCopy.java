package aQute.bnd.make;

import aQute.bnd.service.MakePlugin;
import aQute.lib.osgi.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class MakeCopy implements MakePlugin {

  public Resource make(Builder builder, String destination, Map<String, String> argumentsOnMake) throws Exception {
    String type = argumentsOnMake.get("type");
    if (!type.equals("copy")) return null;

    String from = argumentsOnMake.get("from");
    if (from == null) {
      String content = argumentsOnMake.get("content");
      if (content == null) throw new IllegalArgumentException("No 'from' or 'content' field in copy " + argumentsOnMake);
      return new EmbeddedResource(content.getBytes("UTF-8"), 0);
    }
    else {

      File f = builder.getFile(from);
      if (f.isFile()) {
        return new FileResource(f);
      }
      else {
        try {
          URL url = new URL(from);
          return new URLResource(url);
        }
        catch (MalformedURLException mfue) {
          // We ignore this
        }
        throw new IllegalArgumentException("Copy source does not exist " + from + " for destination " + destination);
      }
    }
  }

}
