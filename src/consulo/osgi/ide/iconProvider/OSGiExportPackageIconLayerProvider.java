package consulo.osgi.ide.iconProvider;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import consulo.osgi.OSGiIcons;
import consulo.osgi.manifest.BundleManifest;

/**
 * @author VISTALL
 * @since 20:30/27.04.13
 */
public class OSGiExportPackageIconLayerProvider extends OSGiPackageIconLayerProvider
{
	@Override
	protected boolean isApplicable(@NotNull String qName, @NotNull BundleManifest bundleManifest)
	{
		return bundleManifest.exportsPackage(qName);
	}

	@NotNull
	@Override
	public Icon getIcon()
	{
		return OSGiIcons.ExportPackage;
	}
}
