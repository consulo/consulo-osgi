package org.osmorc.settings;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import consulo.ui.annotation.RequiredUIAccess;
import org.jetbrains.annotations.Nls;

import javax.annotation.Nonnull;
import javax.swing.*;


public class FrameworkDefinitionsEditor implements SearchableConfigurable
{
	private FrameworkDefinitionsEditorComponent myComponent;

	@Nls
	public String getDisplayName()
	{
		return "Framework Definitions";
	}

	public String getHelpTopic()
	{
		return "reference.settings.project.osgi.framework.definitions";
	}

	@Nonnull
	public String getId()
	{
		return getHelpTopic();
	}

	public Runnable enableSearch(String option)
	{
		return null;
	}

	@RequiredUIAccess
	public JComponent createComponent()
	{
		myComponent = new FrameworkDefinitionsEditorComponent();
		return myComponent.getMainPanel();
	}

	@RequiredUIAccess
	public boolean isModified()
	{
		return myComponent.isModified();
	}

	@RequiredUIAccess
	public void apply() throws ConfigurationException
	{
		myComponent.applyTo(ApplicationSettings.getInstance());
	}

	@RequiredUIAccess
	public void reset()
	{
		myComponent.resetTo(ApplicationSettings.getInstance());
	}

	@RequiredUIAccess
	public void disposeUIResources()
	{
		if(myComponent != null)
		{
			myComponent.dispose();
			myComponent = null;
		}
	}
}
