package consulo.osgi.ide.iconProvider;

import javax.annotation.Nonnull;

import consulo.osgi.OSGiIcons;
import consulo.osgi.manifest.BundleManifest;
import consulo.ui.image.Image;

/**
 * @author VISTALL
 * @since 21:14/27.04.13
 */
public class OSGiImportPackageIconLayerProvider extends OSGiPackageIconLayerProvider
{
	@Override
	protected boolean isApplicable(@Nonnull String qName, @Nonnull BundleManifest bundleManifest)
	{
		return false;
	}

	@Nonnull
	@Override
	public Image getIcon()
	{
		return OSGiIcons.ImportPackage;
	}
}
