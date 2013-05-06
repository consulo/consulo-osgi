package org.jetbrains.osgi.compiler.oldImpl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.osgi.compiler.ManifestProviderWithLocation;

/**
 * @author VISTALL
 * @since 14:07/29.04.13
 */
public class BndFileManifestProvider extends ManifestProviderWithLocation {
  public BndFileManifestProvider() {
    super("$MODULE_DIR$/bnd.bnd");
  }

  @NotNull
  @Override
  public String getHeaderName() {
    return "Use existing bnd file";
  }
}
