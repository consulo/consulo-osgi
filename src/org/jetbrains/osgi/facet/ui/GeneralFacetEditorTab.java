package org.jetbrains.osgi.facet.ui;

import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBRadioButton;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osgi.compiler.ManifestProvider;
import org.jetbrains.osgi.compiler.ManifestProviderConfigurable;
import org.jetbrains.osgi.facet.OSGiFacetConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

/**
 * @author VISTALL
 * @since 18:03/29.04.13
 */
public class GeneralFacetEditorTab extends FacetEditorTab {
  private static final Key<ManifestProvider> MANIFEST_PROVIDER_KEY = Key.create("manifest-provider-key");
  private static final Key<ManifestProviderConfigurable> MANIFEST_PROVIDER_CONFIGURABLE_KEY =
    Key.create("manifest-provider-configurable-key");

  private JPanel myRootPanel;
  private JPanel myRoot;
  private JCheckBox myDoNotSynchronizeFacetCheckBox;
  private TextFieldWithBrowseButton myOsgiInfPane;
  private JPanel myPanelForProviders;

  private final OSGiFacetConfiguration myConfiguration;
  private final Module myModule;
  private final ButtonGroup myButtonGroup = new ButtonGroup();

  private ManifestProvider myActiveProvider;

  public GeneralFacetEditorTab(OSGiFacetConfiguration configuration, Module module) {
    this.myConfiguration = configuration;
    myModule = module;
    this.myActiveProvider = configuration.getActiveManifestProvider();

    ActionListener listener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JBRadioButton source = (JBRadioButton)e.getSource();

        Enumeration<AbstractButton> elements = myButtonGroup.getElements();
        while (elements.hasMoreElements()) {
          AbstractButton button = elements.nextElement();

          ManifestProviderConfigurable configurable = getData(button, MANIFEST_PROVIDER_CONFIGURABLE_KEY);

          boolean val = button == source;
          configurable.setEnabled(val);
          if (val) {
            myActiveProvider = getData(button, MANIFEST_PROVIDER_KEY);
          }
        }
      }
    };

    myOsgiInfPane.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        selectOsgiInfo(myOsgiInfPane);
      }
    });

    myOsgiInfPane.setText(FileUtil.toSystemDependentName(myConfiguration.getOsgiInfLocation()));

    ManifestProvider[] manifestProviders = configuration.getManifestProviders();
    myPanelForProviders.setLayout(new GridLayoutManager(manifestProviders.length, 1));

    for (int i = 0; i < manifestProviders.length; i++) {
      ManifestProvider provider = manifestProviders[i];
      JPanel panel = new JPanel(new BorderLayout());

      ManifestProviderConfigurable configurable = provider.createConfigurable(module);

      JBRadioButton button = new JBRadioButton(configurable.getHeaderName());
      button.addActionListener(listener);

      putData(button, MANIFEST_PROVIDER_KEY, provider);
      putData(button, MANIFEST_PROVIDER_CONFIGURABLE_KEY, configurable);

      myButtonGroup.add(button);
      panel.add(button, BorderLayout.NORTH);

      JComponent component = configurable.createComponent();

      if (component != null) {
        panel.add(component, BorderLayout.CENTER);
        configurable.setEnabled(false);
      }

      myPanelForProviders.add(panel, new GridConstraints(i, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                                                         GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null,
                                                         null));
    }

    Enumeration<AbstractButton> elements = myButtonGroup.getElements();
    while (elements.hasMoreElements()) {
      AbstractButton button = elements.nextElement();

      ManifestProvider provider = getData(button, MANIFEST_PROVIDER_KEY);

      if (myConfiguration.isActive(provider)) {
        button.setSelected(true);

        getData(button, MANIFEST_PROVIDER_CONFIGURABLE_KEY).setEnabled(true);
      }
    }
  }

  private void selectOsgiInfo(TextFieldWithBrowseButton field) {
    VirtualFile currentFile = LocalFileSystem.getInstance().findFileByPath(myConfiguration.getOsgiInfLocation());

    VirtualFile fileLocation =
      FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFolderDescriptor(), myModule.getProject(), currentFile);

    if (fileLocation == null) {
      return;
    }

    field.setText(FileUtil.toSystemDependentName(fileLocation.getPath()));
  }

  @Nls
  @Override
  public String getDisplayName() {
    return "General";
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    return myRootPanel;
  }

  @Override
  public boolean isModified() {
    if (myActiveProvider != myConfiguration.getActiveManifestProvider()) {
      return true;
    }

    // text from myOsgiInfPane already system dependent
    if (!Comparing.equal(FileUtil.toSystemDependentName(myConfiguration.getOsgiInfLocation()), myOsgiInfPane.getText())) {
      return true;
    }

    Enumeration<AbstractButton> elements = myButtonGroup.getElements();
    while (elements.hasMoreElements()) {
      AbstractButton abstractButton = elements.nextElement();

      if (getData(abstractButton, MANIFEST_PROVIDER_CONFIGURABLE_KEY).isModified()) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void apply() throws ConfigurationException {
    myConfiguration.setActiveManifestProvider(myActiveProvider);
    myConfiguration.setOsgiInfLocation(FileUtil.toSystemIndependentName(myOsgiInfPane.getText()));

    Enumeration<AbstractButton> elements = myButtonGroup.getElements();
    while (elements.hasMoreElements()) {
      AbstractButton abstractButton = elements.nextElement();

      getData(abstractButton, MANIFEST_PROVIDER_CONFIGURABLE_KEY).apply();
    }

    myConfiguration.validateAndCreate();
  }

  @Override
  public void reset() {
    myActiveProvider = myConfiguration.getActiveManifestProvider();

    Enumeration<AbstractButton> elements = myButtonGroup.getElements();
    while (elements.hasMoreElements()) {
      AbstractButton abstractButton = elements.nextElement();

      getData(abstractButton, MANIFEST_PROVIDER_CONFIGURABLE_KEY).reset();
    }
  }

  @Override
  public void disposeUIResources() {
  }

  private static <T> void putData(@NotNull JComponent component, @NotNull Key<T> key, T value) {
    component.putClientProperty(key, value);
  }

  @SuppressWarnings("unchecked")
  private static <T> T getData(@NotNull JComponent component, @NotNull Key<T> key) {
    return (T)component.getClientProperty(key);
  }
}
