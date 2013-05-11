package org.jetbrains.osgi.compiler.oldImpl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osgi.compiler.ManifestProviderWithLocation;
import org.jetbrains.osgi.facet.OSGiFacet;
import org.jetbrains.osgi.manifest.BundleManifest;

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
    return "Generate MANIFEST.MF from bnd file";
  }

  @Nullable
  @Override
  protected BundleManifest getBundleManifestImpl(OSGiFacet facet) {
    return null;
  }
}
