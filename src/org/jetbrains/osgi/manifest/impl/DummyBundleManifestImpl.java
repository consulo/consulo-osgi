package org.jetbrains.osgi.manifest.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osgi.manifest.BundleManifest;
import org.osmorc.manifest.lang.psi.Header;
import org.osmorc.manifest.lang.psi.ManifestFile;

/**
 * @author VISTALL
 * @since 15:13/29.04.13
 */
public class DummyBundleManifestImpl extends AbstractBundleManifestImpl {
  public static BundleManifest INSTANCE = new DummyBundleManifestImpl();

  @Nullable
  @Override
  protected Header getHeaderByName(@NotNull String heaaderName) {
    return null;
  }

  @Nullable
  @Override
  public ManifestFile getManifestFile() {
    return null;
  }

  @Override
  public long getModificationCount() {
    return -1;
  }
}
