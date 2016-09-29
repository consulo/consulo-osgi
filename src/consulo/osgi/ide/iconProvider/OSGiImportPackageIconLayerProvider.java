package consulo.osgi.ide.iconProvider;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import consulo.osgi.OSGiIcons;
import consulo.osgi.manifest.BundleManifest;

/**
 * @author VISTALL
 * @since 21:14/27.04.13
 */
public class OSGiImportPackageIconLayerProvider extends OSGiPackageIconLayerProvider
{
	@Override
	protected boolean isApplicable(@NotNull String qName, @NotNull BundleManifest bundleManifest)
	{
		return false;
	}

	@NotNull
	@Override
	public Icon getIcon()
	{
		return OSGiIcons.ImportPackage;
	}
}
