package org.jetbrains.osgi.compiler;

import com.intellij.openapi.extensions.AbstractExtensionPointBean;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.util.ReflectionUtil;
import com.intellij.util.xmlb.annotations.Attribute;

/**
 * @author VISTALL
 * @since 14:32/29.04.13
 */
public class ManifestProviderEP extends AbstractExtensionPointBean {
  public static final ExtensionPointName<ManifestProviderEP> EP_NAME = new ExtensionPointName<ManifestProviderEP>("org.jetbrains.osgi.manifestProvider");

  @Attribute("implementation")
  public String implementation;

  public ManifestProvider createProvider() {
    Class<ManifestProvider> clazz = findClassNoExceptions(implementation);
    if(clazz == null) {
      return null;
    }
    return ReflectionUtil.createInstance(ReflectionUtil.getDefaultConstructor(clazz));
  }
}
