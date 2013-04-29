package org.jetbrains.osmorc2.compiler.impl.ui;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.components.JBLabel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osmorc2.compiler.ManifestProvider;
import org.jetbrains.osmorc2.compiler.ManifestProviderConfigurable;
import org.osmorc.settings.MyErrorText;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 14:30/29.04.13
 */
public class BndManifestProviderConfigurable extends ManifestProviderConfigurable {
  private JPanel myManifestPanel;
  private JPanel myWarningPanel;
  private MyErrorText myErrorText;
  private JButton myCreateButton;
  private JRadioButton myUseProjectDefaultManifestFileLocation;
  private JRadioButton myUseModuleSpecificManifestFileLocation;
  private TextFieldWithBrowseButton myManifestFileChooser;
  private JBLabel myManifestFileLocationJBLabel;
  private JPanel myRootPanel;

  public BndManifestProviderConfigurable(ManifestProvider manifestProvider) {
    super(manifestProvider);
  }

  @Override
  public void setEnabled(boolean val) {
    myUseProjectDefaultManifestFileLocation.setEnabled(val);
    myUseModuleSpecificManifestFileLocation.setEnabled(val);
    myManifestFileChooser.setEnabled(val);
    myCreateButton.setEnabled(val);
  }

  @NotNull
  @Override
  public String getHeaderName() {
    return "Create using bnd and ignore facet configuration";
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    return myRootPanel;
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
