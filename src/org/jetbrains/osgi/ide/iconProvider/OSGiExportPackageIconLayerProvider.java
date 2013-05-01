package org.jetbrains.osgi.ide.iconProvider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osgi.OSGiIcons;
import org.jetbrains.osgi.manifest.BundleManifest;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 20:30/27.04.13
 */
public class OSGiExportPackageIconLayerProvider extends OSGiPackageIconLayerProvider {
  @Nullable
  @Override
  public String getLayerDescription() {
    return "Exported package";
  }
  @Override
  protected boolean isApplicable(@NotNull String qName, @NotNull BundleManifest bundleManifest) {
    return bundleManifest.exportsPackage(qName);
  }

  @NotNull
  @Override
  public Icon getIcon() {
    return OSGiIcons.ExportPackage;
  }
}
