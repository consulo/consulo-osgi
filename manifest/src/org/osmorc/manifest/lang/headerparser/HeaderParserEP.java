package org.osmorc.manifest.lang.headerparser;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.util.ReflectionUtil;
import com.intellij.util.xmlb.annotations.Attribute;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 13:06/27.04.13
 */
public class HeaderParserEP {
  public static final ExtensionPointName<HeaderParserEP> EP_NAME = ExtensionPointName.create("org.osmorc.manifest.headerParser");

  @NotNull
  @Attribute("key")
  public String key;

  @NotNull
  @Attribute("implementationClass")
  public String implementationClass;

  private HeaderParser myParserInstance;

  public HeaderParser getParserInstance() {
    if(myParserInstance == null) {
      try {
        Class<?> clazz = Class.forName(implementationClass);
        myParserInstance = (HeaderParser)ReflectionUtil.createInstance(clazz.getConstructor());
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return myParserInstance;
  }
}
