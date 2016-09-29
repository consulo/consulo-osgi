package consulo.osgi.module.manifest.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import consulo.osgi.manifest.BundleManifest;
import consulo.osgi.module.extension.OSGiModuleExtension;
import consulo.osgi.module.manifest.ManifestProviderWithLocation;

/**
 * @author VISTALL
 * @since 14:07/29.04.13
 */
public class BndFileManifestProvider extends ManifestProviderWithLocation
{
	public BndFileManifestProvider()
	{
		super("$MODULE_DIR$/bnd.bnd");
	}

	@NotNull
	@Override
	public String getHeaderName()
	{
		return "Generate MANIFEST.MF from bnd file";
	}

	@Nullable
	@Override
	protected BundleManifest getBundleManifestImpl(OSGiModuleExtension facet)
	{
		return null;
	}
}
