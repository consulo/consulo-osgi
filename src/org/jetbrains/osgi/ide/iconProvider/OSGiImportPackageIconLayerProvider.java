package org.jetbrains.osgi.ide.iconProvider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osgi.OSGiIcons;
import org.jetbrains.osgi.manifest.BundleManifest;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 21:14/27.04.13
 */
public class OSGiImportPackageIconLayerProvider extends OSGiPackageIconLayerProvider {
  @Nullable
  @Override
  public String getLayerDescription() {
    return "Imported Package";
  }

  @Override
  protected boolean isApplicable(@NotNull String qName, @NotNull BundleManifest bundleManifest) {
    return false;
  }

  @NotNull
  @Override
  public Icon getIcon() {
    return OSGiIcons.ImportPackage;
  }
}
