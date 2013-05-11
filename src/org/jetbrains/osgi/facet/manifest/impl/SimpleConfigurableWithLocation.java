package org.jetbrains.osgi.facet.manifest.impl;

import com.intellij.openapi.components.PathMacroManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osgi.facet.manifest.ManifestProviderConfigurable;
import org.jetbrains.osgi.facet.manifest.ManifestProviderWithLocation;
import org.jetbrains.osgi.facet.manifest.impl.ui.LabelAndFileLocationPanel;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 14:34/30.04.13
 */
public class SimpleConfigurableWithLocation extends ManifestProviderConfigurable<ManifestProviderWithLocation> {
  private final String myHeaderName;
  private final LabelAndFileLocationPanel myPanel;
  private final Module myModule;

  public SimpleConfigurableWithLocation(String headerName, ManifestProviderWithLocation manifestProvider, Module module) {
    super(manifestProvider);
    myHeaderName = headerName;
    myModule = module;
    myPanel = new LabelAndFileLocationPanel();

    String path = PathMacroManager.getInstance(module).expandPath(myManifestProvider.getLocation());

    myPanel.getTextField().setText(FileUtil.toSystemDependentName(path));
  }

  @Override
  public void setEnabled(boolean val) {
    myPanel.setEnabled(val);
  }

  @NotNull
  @Override
  public String getHeaderName() {
    return myHeaderName;
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    return myPanel;
  }

  @Override
  public boolean isModified() {
    String newPath = PathMacroManager.getInstance(myModule).collapsePath(myPanel.getTextField().getText());

    return !newPath.equals(myManifestProvider.getLocation());
  }

  @Override
  public void apply() throws ConfigurationException {
    String newPath = PathMacroManager.getInstance(myModule).collapsePath(myPanel.getTextField().getText());

    myManifestProvider.setLocation(FileUtil.toSystemIndependentName(newPath));
  }

  @Override
  public void reset() {
  }

  @Override
  public void disposeUIResources() {
  }
}
