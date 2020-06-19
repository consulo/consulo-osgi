package consulo.osgi.module.extension;

import consulo.java.roots.SpecialDirUtil;
import consulo.module.extension.impl.ModuleExtensionImpl;
import consulo.osgi.OSGiConstants;
import consulo.osgi.manifest.BundleManifest;
import consulo.osgi.module.manifest.ManifestProvider;
import consulo.osgi.module.manifest.impl.ui.UseExistingManifestManifestProvider;
import consulo.roots.ModuleRootLayer;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 16:02/30.05.13
 */
public class OSGiModuleExtension extends ModuleExtensionImpl<OSGiModuleExtension>
{
	public OSGiModuleExtension(@Nonnull String id, @Nonnull ModuleRootLayer module)
	{
		super(id, module);
	}

	@Nonnull
	public BundleManifest getManifest()
	{
		final ManifestProvider activeManifestProvider = new UseExistingManifestManifestProvider();
		return activeManifestProvider.getBundleManifest(this);
	}

	public String getOSGiInf()
	{
		return SpecialDirUtil.getSpecialDirLocation(getModule(), OSGiConstants.OSGI_INFO_ROOT);
	}

	public String getMETAInf()
	{
		return SpecialDirUtil.getSpecialDirLocation(getModule(), SpecialDirUtil.META_INF);
	}
}
