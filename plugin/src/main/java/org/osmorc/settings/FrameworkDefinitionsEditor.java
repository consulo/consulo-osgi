package org.osmorc.settings;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import javax.annotation.Nonnull;

import javax.swing.*;


public class FrameworkDefinitionsEditor implements SearchableConfigurable {
  private FrameworkDefinitionsEditorComponent myComponent;

  public FrameworkDefinitionsEditor() {
  }

  @Nls
  public String getDisplayName() {
    return "Framework Definitions";
  }

  public String getHelpTopic() {
    return "reference.settings.project.osgi.framework.definitions";
  }

  @Nonnull
  public String getId() {
    return getHelpTopic();
  }

  public Runnable enableSearch(String option) {
    return null;
  }

  public JComponent createComponent() {
    myComponent = new FrameworkDefinitionsEditorComponent();
    return myComponent.getMainPanel();
  }

  public boolean isModified() {
    return myComponent.isModified();
  }

  public void apply() throws ConfigurationException {
    myComponent.applyTo(ApplicationSettings.getInstance());
  }

  public void reset() {
    myComponent.resetTo(ApplicationSettings.getInstance());
  }

  public void disposeUIResources() {
    myComponent.dispose();
    myComponent = null;
  }
}