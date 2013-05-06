package org.jetbrains.osgi.compiler.oldImpl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.osgi.compiler.ManifestProviderWithLocation;

/**
 * @author VISTALL
 * @since 14:06/29.04.13
 */
public class ManifestFileAsManifestProvider extends ManifestProviderWithLocation {
  public ManifestFileAsManifestProvider() {
    super("$MODULE_DIR$/META-INF/MANIFEST.MF");
  }

  @NotNull
  @Override
  public String getHeaderName() {
    return "Use existing manifest";
  }
}
