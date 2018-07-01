package consulo.osgi.ide.iconProvider;

import javax.annotation.Nonnull;
import consulo.osgi.OSGiIcons;
import consulo.osgi.manifest.BundleManifest;
import consulo.ui.image.Image;

/**
 * @author VISTALL
 * @since 20:30/27.04.13
 */
public class OSGiExportPackageIconLayerProvider extends OSGiPackageIconLayerProvider
{
	@Override
	protected boolean isApplicable(@Nonnull String qName, @Nonnull BundleManifest bundleManifest)
	{
		return bundleManifest.exportsPackage(qName);
	}

	@Nonnull
	@Override
	public Image getIcon()
	{
		return OSGiIcons.ExportPackage;
	}
}
