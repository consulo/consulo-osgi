package org.jetbrains.osgi.module.extension;

import com.intellij.openapi.components.PathMacroManager;
import com.intellij.openapi.module.Module;
import org.consulo.module.extension.impl.ModuleExtensionImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.osgi.OSGiConstants;
import org.jetbrains.osgi.facet.manifest.ManifestProvider;
import org.jetbrains.osgi.facet.manifest.impl.UseExistingManifestManifestProvider;
import org.jetbrains.osgi.manifest.BundleManifest;

/**
 * @author VISTALL
 * @since 16:02/30.05.13
 */
public class OSGiModuleExtension extends ModuleExtensionImpl<OSGiModuleExtension> {
  public OSGiModuleExtension(@NotNull String id, @NotNull Module module) {
    super(id, module);
  }

  @NotNull
  public BundleManifest getManifest() {
    final ManifestProvider activeManifestProvider = new UseExistingManifestManifestProvider();
    return activeManifestProvider.getBundleManifest(this);
  }

  public String getOSGiInf() {
    return PathMacroManager.getInstance(getModule()).expandPath(OSGiConstants.DEFAULT_OSGI_INF_ROOT_LOCATION);
  }

  public String getMETAInf() {
    return PathMacroManager.getInstance(getModule()).expandPath(OSGiConstants.DEFAULT_META_INF_ROOT_LOCATION);
  }
}
