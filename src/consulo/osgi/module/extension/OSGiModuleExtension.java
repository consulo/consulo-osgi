package consulo.osgi.module.extension;

import org.jetbrains.annotations.NotNull;
import consulo.extension.impl.ModuleExtensionImpl;
import consulo.java.roots.SpecialDirUtil;
import consulo.osgi.OSGiConstants;
import consulo.osgi.manifest.BundleManifest;
import consulo.osgi.module.manifest.ManifestProvider;
import consulo.osgi.module.manifest.impl.ui.UseExistingManifestManifestProvider;
import consulo.roots.ModuleRootLayer;

/**
 * @author VISTALL
 * @since 16:02/30.05.13
 */
public class OSGiModuleExtension extends ModuleExtensionImpl<OSGiModuleExtension>
{
	public OSGiModuleExtension(@NotNull String id, @NotNull ModuleRootLayer module)
	{
		super(id, module);
	}

	@NotNull
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
