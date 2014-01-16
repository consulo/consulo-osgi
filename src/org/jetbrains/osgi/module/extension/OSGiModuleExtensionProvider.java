package org.jetbrains.osgi.module.extension;

import javax.swing.Icon;

import org.consulo.module.extension.ModuleExtensionProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osgi.OSGiIcons;
import com.intellij.openapi.module.Module;

/**
 * @author VISTALL
 * @since 16:03/30.05.13
 */
public class OSGiModuleExtensionProvider implements ModuleExtensionProvider<OSGiModuleExtension, OSGiMutableModuleExtension> {
  @Nullable
  @Override
  public Icon getIcon() {
    return OSGiIcons.FacetType;
  }

  @NotNull
  @Override
  public String getName() {
    return "OSGi";
  }

  @NotNull
  @Override
  public OSGiModuleExtension createImmutable(@NotNull String s, @NotNull Module module) {
    return new OSGiModuleExtension(s, module);
  }

  @NotNull
  @Override
  public OSGiMutableModuleExtension createMutable(@NotNull String s, @NotNull Module module, @NotNull OSGiModuleExtension moduleExtension) {
    return new OSGiMutableModuleExtension(s, module, moduleExtension);
  }
}
