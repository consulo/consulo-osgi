import aQute.bnd.make.component.ComponentAnnotationReader;
import aQute.lib.osgi.Clazz;
import aQute.lib.osgi.FileResource;
import aQute.libg.reporter.Reporter;
import consulo.osgi.compiler.artifact.bndTools.serviceComponent.BndServiceComponentUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author VISTALL
 * @since 22:57/28.04.13
 */
public class MyTest {
  public static void main(String[] args) throws Exception {
    List<File> files = new ArrayList<File>();
    collectFiles(files, new File("K:\\simpleMaven\\simpleMavenArtifacts\\simpleMavenA\\target\\classes"));

    for (File file : files) {
      Clazz clazz = new Clazz(file.getAbsolutePath(), new FileResource(file));



      final Map<String, String> definition = ComponentAnnotationReader.getDefinition(clazz, new Reporter() {
        @Override
        public void error(String s, Object... objects) {
        }

        @Override
        public void warning(String s, Object... objects) {
        }

        @Override
        public void progress(String s, Object... objects) {
        }

        @Override
        public void trace(String s, Object... objects) {
        }

        @Override
        public List<String> getWarnings() {
          return null;
        }

        @Override
        public List<String> getErrors() {
          return null;
        }

        @Override
        public boolean isPedantic() {
          return false;
        }
      });
      if(definition == null) {
        continue;
      }

      String name = BndServiceComponentUtil.getName(definition, clazz);

      System.out.println(file.getAbsolutePath() + " \n" + BndServiceComponentUtil.toXml(definition, name) + "\n name: " + name);
    }
  }

  private static void collectFiles(List<File> files, File file) {
    if (file.isDirectory()) {
      for (File t : file.listFiles()) {
        collectFiles(files, t);
      }
    }
    else if (file.getName().endsWith(".class")) {
      files.add(file);
    }
  }
}
