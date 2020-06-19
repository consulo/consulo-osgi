package org.osmorc.settings;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import consulo.ui.annotation.RequiredUIAccess;
import org.jetbrains.annotations.Nls;

import javax.annotation.Nonnull;
import javax.swing.*;

/**
 * @author <a href="janthomae@janthomae.de">Jan Thom&auml;</a>
 * @version $Id:$
 */
public class ProjectSettingsEditor implements SearchableConfigurable
{

	private ProjectSettingsEditorComponent component;
	private final Project myProject;

	public ProjectSettingsEditor(Project project)
	{
		myProject = project;
	}

	@Nls
	public String getDisplayName()
	{
		return "OSGi";
	}

	public String getHelpTopic()
	{
		return "reference.settings.project.osgi.project.settings";
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
		component = new ProjectSettingsEditorComponent(myProject);
		return component.getMainPanel();
	}

	@RequiredUIAccess
	public void disposeUIResources()
	{
		if(component != null)
		{
			component.dispose();
			component = null;
		}
	}

	@RequiredUIAccess
	public boolean isModified()
	{
		return component != null && component.isModified();
	}

	@RequiredUIAccess
	public void apply() throws ConfigurationException
	{
		component.applyTo(ProjectSettings.getInstance(myProject));
	}

	@RequiredUIAccess
	public void reset()
	{
		component.resetTo(ProjectSettings.getInstance(myProject));
	}
}
