package org.jetbrains.osgi.module.extension;

import org.consulo.java.platform.roots.SpecialDirUtil;
import org.consulo.module.extension.impl.ModuleExtensionImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.osgi.OSGiConstants;
import org.jetbrains.osgi.facet.manifest.ManifestProvider;
import org.jetbrains.osgi.facet.manifest.impl.UseExistingManifestManifestProvider;
import org.jetbrains.osgi.manifest.BundleManifest;
import com.intellij.openapi.roots.ModuleRootLayer;

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
