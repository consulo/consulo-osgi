package org.jetbrains.osmorc2.compiler.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osmorc2.compiler.ManifestProvider;
import org.jetbrains.osmorc2.compiler.ManifestProviderConfigurable;
import org.jetbrains.osmorc2.compiler.impl.ui.ManifestFileAsManifestProviderConfigurable;
import org.jetbrains.osmorc2.manifest.BundleManifest;

import java.util.Map;

/**
 * @author VISTALL
 * @since 14:06/29.04.13
 */
public class ManifestFileAsManifestProvider extends ManifestProvider {

  @NotNull
  @Override
  public ManifestProviderConfigurable createConfigurable() {
    return new ManifestFileAsManifestProviderConfigurable(this);
  }

  @Nullable
  @Override
  protected BundleManifest getBundleManifestImpl() {
    return null;
  }

  @Override
  public void getBuildProperties(@NotNull Map<String, String> map) {
  }
}
