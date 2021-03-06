package consulo.osgi.module.manifest.impl;

import javax.annotation.Nullable;
import javax.swing.JComponent;

import javax.annotation.Nonnull;

import com.intellij.openapi.components.PathMacroManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.io.FileUtil;
import consulo.osgi.module.manifest.ManifestProviderConfigurable;
import consulo.osgi.module.manifest.ManifestProviderWithLocation;
import consulo.osgi.module.manifest.impl.ui.LabelAndFileLocationPanel;

/**
 * @author VISTALL
 * @since 14:34/30.04.13
 */
public class SimpleConfigurableWithLocation extends ManifestProviderConfigurable<ManifestProviderWithLocation>
{
	private final String myHeaderName;
	private final LabelAndFileLocationPanel myPanel;
	private final Module myModule;

	public SimpleConfigurableWithLocation(String headerName, ManifestProviderWithLocation manifestProvider, Module module)
	{
		super(manifestProvider);
		myHeaderName = headerName;
		myModule = module;
		myPanel = new LabelAndFileLocationPanel();

		String path = PathMacroManager.getInstance(module).expandPath(myManifestProvider.getLocation());

		myPanel.getTextField().setText(FileUtil.toSystemDependentName(path));
	}

	@Override
	public void setEnabled(boolean val)
	{
		myPanel.setEnabled(val);
	}

	@Nonnull
	@Override
	public String getHeaderName()
	{
		return myHeaderName;
	}

	@Nullable
	@Override
	public JComponent createComponent()
	{
		return myPanel;
	}

	@Override
	public boolean isModified()
	{
		String newPath = PathMacroManager.getInstance(myModule).collapsePath(myPanel.getTextField().getText());

		return !newPath.equals(myManifestProvider.getLocation());
	}

	@Override
	public void apply() throws ConfigurationException
	{
		String newPath = PathMacroManager.getInstance(myModule).collapsePath(myPanel.getTextField().getText());

		myManifestProvider.setLocation(FileUtil.toSystemIndependentName(newPath));
	}

	@Override
	public void reset()
	{
	}

	@Override
	public void disposeUIResources()
	{
	}
}
