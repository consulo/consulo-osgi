package aQute.bnd.make;

import aQute.bnd.build.ProjectBuilder;
import aQute.bnd.service.MakePlugin;
import aQute.lib.osgi.*;

import java.io.File;
import java.util.Map;
import java.util.regex.Pattern;

public class MakeBnd implements MakePlugin, Constants {
  final static Pattern JARFILE = Pattern.compile("(.+)\\.(jar|ipa)");

  public Resource make(Builder builder, String destination, Map<String, String> argumentsOnMake) throws Exception {
    String type = (String)argumentsOnMake.get("type");
    if (!"bnd".equals(type)) return null;

    String recipe = (String)argumentsOnMake.get("recipe");
    if (recipe == null) {
      builder.error("No recipe specified on a make instruction for " + destination);
      return null;
    }
    File bndfile = builder.getFile(recipe);
    if (bndfile.isFile()) {
      // We do not use a parent because then we would
      // build ourselves again. So we can not blindly
      // inherit the properties.
      Builder bchild = builder.getSubBuilder();
      bchild.removeBundleSpecificHeaders();

      // We must make sure that we do not include ourselves again!
      bchild.setProperty(Analyzer.INCLUDE_RESOURCE, "");
      bchild.setProperty(Analyzer.INCLUDERESOURCE, "");
      bchild.setProperties(bndfile, builder.getBase());

      Jar jar = bchild.build();
      Jar dot = builder.getTarget();

      if (builder.hasSources()) {
        for (String key : jar.getResources().keySet()) {
          if (key.startsWith("OSGI-OPT/src")) dot.putResource(key, (Resource)jar.getResource(key));
        }
      }
      builder.getInfo(bchild, bndfile.getName() + ": ");
      String debug = bchild.getProperty(DEBUG);
      if (Processor.isTrue(debug)) {
        if (builder instanceof ProjectBuilder) {
          ProjectBuilder pb = (ProjectBuilder)builder;
          File target = pb.getProject().getTarget();
          String bsn = bchild.getBsn();
          File output = new File(target, bsn + ".jar");
          jar.write(output);
          pb.getProject().getWorkspace().changedFile(output);
        }
      }
      return new JarResource(jar);
    }
    else {
      return null;
    }
  }

}
