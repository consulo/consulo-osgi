package org.jetbrains.osgi.facet.manifest.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osgi.facet.manifest.ManifestProviderWithLocation;
import org.jetbrains.osgi.manifest.BundleManifest;
import org.jetbrains.osgi.module.extension.OSGiModuleExtension;

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
  protected BundleManifest getBundleManifestImpl(OSGiModuleExtension facet) {
    return null;
  }
}
