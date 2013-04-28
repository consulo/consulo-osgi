import aQute.bnd.make.component.ComponentAnnotationReader;
import aQute.lib.osgi.Clazz;
import aQute.lib.osgi.FileResource;
import aQute.libg.reporter.Reporter;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author VISTALL
 * @since 22:57/28.04.13
 */
public class MyTest {
  public static void main(String[] args) throws Exception{
    File file = new File("G:\\Users\\VISTALL\\workspace\\test\\bin\\org\\example\\ExampleComponent.class");

    Clazz clazz = new Clazz("G:\\Users\\VISTALL\\workspace\\test\\bin\\org\\example\\ExampleComponent.class", new FileResource(file));


    final Map<String,String> definition = ComponentAnnotationReader.getDefinition(clazz, new Reporter() {
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
    for (Map.Entry<String, String> entry : definition.entrySet()) {
      System.out.println(entry);
    }
  }
}
