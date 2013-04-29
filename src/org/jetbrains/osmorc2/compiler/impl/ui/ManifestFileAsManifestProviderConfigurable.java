package org.jetbrains.osmorc2.compiler.impl.ui;

import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osmorc2.compiler.ManifestProvider;
import org.jetbrains.osmorc2.compiler.ManifestProviderConfigurable;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 15:12/29.04.13
 */
public class ManifestFileAsManifestProviderConfigurable extends ManifestProviderConfigurable {
  public ManifestFileAsManifestProviderConfigurable(ManifestProvider manifestProvider) {
    super(manifestProvider);
  }

  @NotNull
  @Override
  public String getHeaderName() {
    return "Use existing manifest and bundle using facet configuration";
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    return null;
  }

  @Override
  public boolean isModified() {
    return false;
  }

  @Override
  public void apply() throws ConfigurationException {
  }

  @Override
  public void reset() {
  }

  @Override
  public void disposeUIResources() {
  }
}
