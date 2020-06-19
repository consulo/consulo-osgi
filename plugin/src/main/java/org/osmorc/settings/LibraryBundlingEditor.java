package org.osmorc.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import consulo.ui.annotation.RequiredUIAccess;
import org.jetbrains.annotations.Nls;

import javax.annotation.Nonnull;
import javax.swing.*;

/**
 * @author <a href="janthomae@janthomae.de">Jan Thom&auml;</a>
 * @version $Id:$
 */
public class LibraryBundlingEditor implements SearchableConfigurable, Configurable.NoScroll
{
	private LibraryBundlingEditorComponent myComponent;

	@Nls
	public String getDisplayName()
	{
		return "Library Bundling";
	}

	public String getHelpTopic()
	{
		return "reference.settings.project.osgi.library.bundling";
	}

	@Nonnull
	public String getId()
	{
		return getHelpTopic();
	}
	@RequiredUIAccess
	public JComponent createComponent()
	{
		myComponent = new LibraryBundlingEditorComponent();
		return myComponent.getMainPanel();
	}

	@RequiredUIAccess
	public boolean isModified()
	{
		return myComponent != null && myComponent.isModified();
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
