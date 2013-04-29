package org.jetbrains.osmorc2.compiler;

import com.intellij.openapi.options.UnnamedConfigurable;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 14:19/29.04.13
 */
public abstract class ManifestProviderConfigurable implements UnnamedConfigurable {
  protected final ManifestProvider myManifestProvider;

  protected ManifestProviderConfigurable(ManifestProvider manifestProvider) {
    myManifestProvider = manifestProvider;
  }

  public void setEnabled(boolean val) {

  }

  @NotNull
  public abstract String getHeaderName();
}
