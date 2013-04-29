/*
 * Copyright (c) 2007-2009, Osmorc Development Team
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright notice, this list
 *       of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice, this
 *       list of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *     * Neither the name of 'Osmorc Development Team' nor the names of its contributors may be
 *       used to endorse or promote products derived from this software without specific
 *       prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.osmorc.facet.ui;

import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.*;
import com.intellij.ui.PanelWithAnchor;
import com.intellij.ui.components.JBRadioButton;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osmorc2.OsgiConstants;
import org.jetbrains.osmorc2.compiler.ManifestProvider;
import org.jetbrains.osmorc2.compiler.ManifestProviderConfigurable;
import org.osgi.framework.Constants;
import org.osgi.framework.Version;
import org.osmorc.facet.OsmorcFacetConfiguration;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.Attributes;

/**
 * The facet editor tab which is used to set up general Osmorc facet settings.
 *
 * @author <a href="mailto:janthomae@janthomae.de">Jan Thom&auml;</a>
 * @author Robert F. Beeger (robert@beeger.net)
 */
public class OsmorcFacetGeneralEditorTab extends FacetEditorTab implements PanelWithAnchor {
  static final Key<Boolean> MANUAL_MANIFEST_EDITING_KEY = Key.create("MANUAL_MANIFEST_EDITING");
  static final Key<Boolean> BND_CREATION_KEY = Key.create("BND_CREATION");
  static final Key<Boolean> BUNDLOR_CREATION_KEY = Key.create("BUNDLOR_CREATION");

  private static final Key<ManifestProvider> MANIFEST_PROVIDER_KEY = Key.create("manifest-provider-key");
  private static final Key<ManifestProviderConfigurable> MANIFEST_PROVIDER_CONFIGURABLE_KEY = Key.create("manifest-provider-configurable-key");

  private JPanel myRoot;
  private JCheckBox myDoNotSynchronizeFacetCheckBox;
  private TextFieldWithBrowseButton myOsgiInfPane;
  private JPanel myPanelForProviders;

  private boolean myModified;
  private final FacetEditorContext myEditorContext;
  private final Module myModule;
  private JComponent myAnchor;

  private ButtonGroup myButtonGroup = new ButtonGroup();

  public OsmorcFacetGeneralEditorTab(OsmorcFacetConfiguration configuration, FacetEditorContext editorContext) {
    myEditorContext = editorContext;
    myModule = editorContext.getModule();
    myOsgiInfPane.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        selectOsgiInfo(myOsgiInfPane);
      }
    });

    ActionListener listener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JBRadioButton source = (JBRadioButton) e.getSource();

        Enumeration<AbstractButton> elements = myButtonGroup.getElements();
        while (elements.hasMoreElements()) {
          AbstractButton button = elements.nextElement();

          ManifestProviderConfigurable configurable = getData(button, MANIFEST_PROVIDER_CONFIGURABLE_KEY);

          configurable.setEnabled(button != source);
        }
      }
    };

   /* ManifestProvider[] manifestProviders = configuration.getFacet().getManifestProviders();
    myPanelForProviders.setLayout(new GridLayoutManager(manifestProviders.length, 1));

    for (int i = 0; i < manifestProviders.length; i++) {
      ManifestProvider provider = manifestProviders[i];
      JPanel panel = new JPanel(new BorderLayout());

      ManifestProviderConfigurable configurable = provider.createConfigurable();

      JBRadioButton button = new JBRadioButton(configurable.getHeaderName(), provider.isActive());
      button.addActionListener(listener);

      putData(button, MANIFEST_PROVIDER_KEY, provider);
      putData(button, MANIFEST_PROVIDER_CONFIGURABLE_KEY, configurable);

      myButtonGroup.add(button);
      panel.add(button, BorderLayout.NORTH);

      JComponent component = configurable.createComponent();
      if(component != null) {
        panel.add(component, BorderLayout.CENTER);
      }

      myPanelForProviders.add(panel, new GridConstraints(i, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    }
         */
    /*-Enumeration < AbstractButton > elements = myButtonGroup.getElements();
    while (elements.hasMoreElements()) {
      AbstractButton button = elements.nextElement();

      ManifestProviderConfigurable configurable = getData(button, MANIFEST_PROVIDER_CONFIGURABLE_KEY);

      configurable.setEnabled(button != source);
    }  -* /

    UserActivityWatcher watcher = new UserActivityWatcher();
    watcher.addUserActivityListener(new UserActivityListener() {
      public void stateChanged() {
        myModified = true;
       // checkFileExisting();
      }
    });

    watcher.register(myRoot);

   /* myUseProjectDefaultManifestFileLocation.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        onUseProjectDefaultManifestFileLocationChanged();
      }
    });
    myCreateButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        tryCreateBundleManifest();
        checkFileExisting();
      }
    });  */

    setAnchor(myPanelForProviders);
  }

  private void updateGui() {



   // checkFileExisting();
  }

  /*private void onUseProjectDefaultManifestFileLocationChanged() {
    myManifestFileChooser.setEnabled(!myUseProjectDefaultManifestFileLocation.isSelected());
    myModified = true;
  }

  private void onManifestFileSelect() {
    VirtualFile[] roots = getContentRoots(myModule);
    VirtualFile currentFile = findFileInContentRoots(myManifestFileChooser.getText(), myModule);

    VirtualFile manifestFileLocation =
      FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFileOrFolderDescriptor(), myEditorContext.getProject(), currentFile);

    if (manifestFileLocation != null) {
      for (VirtualFile root : roots) {
        String relativePath = VfsUtilCore.getRelativePath(manifestFileLocation, root, File.separatorChar);
        if (relativePath != null) {
          // okay, it resides inside one of our content roots, so far so good.
          if (manifestFileLocation.isDirectory()) {
            // its a folder, so add "MANIFEST.MF" to it as a default.
            relativePath += "/MANIFEST.MF";
          }

          myManifestFileChooser.setText(relativePath);
          break;
        }
      }
    }
  }        */

  @Override
  public JComponent getAnchor() {
    return myAnchor;
  }

  @Override
  public void setAnchor(@Nullable JComponent anchor) {
    this.myAnchor = anchor;
   /* myBundlorFileLocationJBLabel.setAnchor(anchor);
    myBndFileLocationJBLabel.setAnchor(anchor);
    myManifestFileLocationJBLabel.setAnchor(anchor);    */
  }

  private static VirtualFile[] getContentRoots(Module module) {
    return ModuleRootManager.getInstance(module).getContentRoots();
  }

  @Nls
  public String getDisplayName() {
    return "General";
  }

  public JComponent createComponent() {
    return myRoot;
  }

  public boolean isModified() {
    return myModified;
  }

  private void selectOsgiInfo(TextFieldWithBrowseButton field) {
      VirtualFile[] roots = getContentRoots(myModule);
      VirtualFile currentFile = findFileInContentRoots(field.getText(), myModule);

    VirtualFile fileLocation =
      FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFolderDescriptor(), myEditorContext.getProject(), currentFile);

    if (fileLocation != null) {
      for (VirtualFile root : roots) {
        String relativePath = VfsUtilCore
          .getRelativePath(fileLocation, root, File.separatorChar);
        if (relativePath != null) {
          if(relativePath.equals(OsgiConstants.OSGI_INFO_ROOT)) {
            relativePath = "";
          }
          else if(relativePath.endsWith(OsgiConstants.OSGI_INFO_ROOT)) {
            relativePath = relativePath.substring(0, relativePath.length() - OsgiConstants.OSGI_INFO_ROOT.length() - 1);
          }
          field.setText(relativePath);
          break;
        }
      }
    }
  }

  private void selectBuildFile(TextFieldWithBrowseButton field) {
    VirtualFile[] roots = getContentRoots(myModule);
    VirtualFile currentFile = findFileInContentRoots(field.getText(), myModule);

    VirtualFile fileLocation =
      FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor(), myEditorContext.getProject(), currentFile);


    if (fileLocation != null) {
      for (VirtualFile root : roots) {
        String relativePath = VfsUtilCore
          .getRelativePath(fileLocation, root, File.separatorChar);
        if (relativePath != null) {
          field.setText(relativePath);
          break;
        }
      }
    }
    updateGui();
  }

  public void apply() {
    /*OsmorcFacetConfiguration configuration =
      (OsmorcFacetConfiguration)myEditorContext.getFacet().getConfiguration();
    configuration.setManifestGenerationMode(
      myControlledByOsmorcRadioButton.isSelected() ? OsmorcFacetConfiguration.ManifestGenerationMode.OsmorcControlled :
      myUseBndFileRadioButton.isSelected() ? OsmorcFacetConfiguration.ManifestGenerationMode.Bnd :
      myUseBundlorFileRadioButton.isSelected() ? OsmorcFacetConfiguration.ManifestGenerationMode.Bundlor :
      OsmorcFacetConfiguration.ManifestGenerationMode.Manually);

    configuration.setManifestLocation(myManifestFileChooser.getText());
    configuration.setUseProjectDefaultManifestFileLocation(myUseProjectDefaultManifestFileLocation.isSelected());
    configuration.setBndFileLocation(FileUtil.toSystemIndependentName(myBndFile.getText()));
    configuration.setBundlorFileLocation(FileUtil.toSystemIndependentName(myBundlorFile.getText()));
    configuration.setOsgiInfLocation(FileUtil.toSystemIndependentName(myOsgiInfPane.getText()));
    configuration.setDoNotSynchronizeWithMaven(myDoNotSynchronizeFacetCheckBox.isSelected());   */
  }

  public void reset() {
    OsmorcFacetConfiguration configuration =
      (OsmorcFacetConfiguration)myEditorContext.getFacet().getConfiguration();

    myOsgiInfPane.setText(FileUtil.toSystemDependentName(configuration.getOsgiInfLocation()));
    myDoNotSynchronizeFacetCheckBox.setSelected(configuration.isDoNotSynchronizeWithMaven());

    updateGui();
  }

  @Override
  public void onTabEntering() {
    super.onTabEntering();
    updateGui();
  }

  public void disposeUIResources() {

  }

  private String getManifestLocation() {

    return null;
  }

  /*private void checkFileExisting() {
    boolean showWarning;
    if (myControlledByOsmorcRadioButton.isSelected() || myUseBndFileRadioButton.isSelected() || myUseBundlorFileRadioButton.isSelected()) {
      showWarning = false;
    }
    else {
      String location = getManifestLocation();
      if (location == null) {
        showWarning = false;
      }
      else {
        VirtualFile file = findFileInContentRoots(location, myModule);
        showWarning = file == null;
      }
    }

    myWarningPanel.setVisible(showWarning);
    myRoot.revalidate();
  }        */

  private void createUIComponents() {
//    myErrorText = new MyErrorText();
//    myErrorText.setError("The manifest file does not exist.");
  }

  private void tryCreateBundleManifest() {

    // check if a manifest path has been set up
    final String manifestPath = getManifestLocation();
    if (StringUtil.isEmpty(manifestPath)) {
      return;
    }

    final VirtualFile[] contentRoots = getContentRoots(myModule);
    if (contentRoots.length > 0) {

      Application application = ApplicationManager.getApplication();

      application.runWriteAction(new Runnable() {
        public void run() {
          try {

            VirtualFile contentRoot = contentRoots[0];
            String completePath = contentRoot.getPath() + File.separator + manifestPath;

            // unify file separators
            completePath = completePath.replace('\\', '/');

            // strip off the last part (its the filename)
            int lastPathSep = completePath.lastIndexOf('/');
            String path = completePath.substring(0, lastPathSep);
            String filename = completePath.substring(lastPathSep + 1);

            // make sure the folders exist
            VfsUtil.createDirectories(path);

            // and get the virtual file for it
            VirtualFile parentFolder = LocalFileSystem.getInstance().refreshAndFindFileByPath(path);

            // some heuristics for bundle name and version
            String bundleName = myModule.getName();
            Version bundleVersion = null;
            int nextDotPos = bundleName.indexOf('.');
            while (bundleVersion == null && nextDotPos >= 0) {
              try {
                bundleVersion = new Version(bundleName.substring(nextDotPos + 1));
                bundleName = bundleName.substring(0, nextDotPos);
              }
              catch (IllegalArgumentException e) {
                // Retry after next dot.
              }
              nextDotPos = bundleName.indexOf('.', nextDotPos + 1);
            }


            VirtualFile manifest = parentFolder.createChildData(this, filename);
            String text = Attributes.Name.MANIFEST_VERSION + ": 1.0.0\n" +
                          Constants.BUNDLE_MANIFESTVERSION + ": 2\n" +
                          Constants.BUNDLE_NAME + ": " + bundleName + "\n" +
                          Constants.BUNDLE_SYMBOLICNAME + ": " + bundleName + "\n" +
                          Constants.BUNDLE_VERSION + ": " +
                          (bundleVersion != null ? bundleVersion.toString() : "1.0.0") +
                          "\n";
            VfsUtil.saveText(manifest, text);
          }
          catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      });
      VirtualFileManager.getInstance().syncRefresh();
    }
  }

  @Override
  public String getHelpTopic() {
    return "reference.settings.module.facet.osgi";
  }

  private static VirtualFile findFileInContentRoots(String fileName, Module module) {
    VirtualFile[] roots = getContentRoots(module);
    VirtualFile currentFile = null;
    for (VirtualFile root : roots) {
      currentFile = VfsUtil.findRelativeFile(fileName, root);
      if (currentFile != null) {
        break;
      }
    }
    return currentFile;
  }

  private static <T> void putData(@NotNull JComponent component, @NotNull Key<T> key, T value) {
    component.putClientProperty(key, value);
  }

  @SuppressWarnings("unchecked")
  private static <T> T getData(@NotNull JComponent component, @NotNull Key<T> key) {
    return (T)component.getClientProperty(key);
  }
}

