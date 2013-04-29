package org.jetbrains.osmorc2.compiler.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osmorc2.compiler.ManifestProvider;
import org.jetbrains.osmorc2.compiler.ManifestProviderConfigurable;
import org.jetbrains.osmorc2.compiler.impl.ui.BndManifestProviderConfigurable;
import org.jetbrains.osmorc2.manifest.BundleManifest;

import java.io.IOException;
import java.util.Map;

/**
 * @author VISTALL
 * @since 14:07/29.04.13
 */
public class BndFileManifestProvider extends ManifestProvider {
  private String myBndFileLocation;

  @NotNull
  @Override
  public ManifestProviderConfigurable createConfigurable() {
    return new BndManifestProviderConfigurable(this);
  }

  @Nullable
  @Override
  protected BundleManifest getBundleManifestImpl() {
    return null;
  }

  @Override
  public void getBuildProperties(@NotNull Map<String, String> map) throws IOException {
    /*File bndFile = findFileInModuleContentRoots(myBndFileLocation, myModule);
    if (bndFile == null || !bndFile.exists() || bndFile.isDirectory()) {
      compileContext.addMessage(CompilerMessageCategory.ERROR,
                                String.format(messagePrefix + "The bnd file \"%s\" for module \"%s\" does not exist.",
                                              configuration.getBndFileLocation(), module.getName()),
                                configuration.getBndFileLocation(), 0, 0);
      return;
    }
    else {
      OrderedProperties props = new OrderedProperties();
      FileInputStream fis = new FileInputStream(bndFile);
      try {
        props.load(fis);
      }
      finally {
        fis.close();
      }
      // copy properties to map
      map.putAll(props.toMap());
    } */
  }
}
