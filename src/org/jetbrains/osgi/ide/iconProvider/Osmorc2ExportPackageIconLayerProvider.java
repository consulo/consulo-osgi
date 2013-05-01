package org.jetbrains.osgi.ide.iconProvider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osgi.OSGiIcons;
import org.osgi.framework.Constants;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 20:30/27.04.13
 */
public class Osmorc2ExportPackageIconLayerProvider extends Osmorc2PackageIconLayerProvider {
  @Nullable
  @Override
  public String getLayerDescription() {
    return "Exported package";
  }

  @NotNull
  @Override
  public String getKeyForManifest() {
    return Constants.EXPORT_PACKAGE;
  }

  @NotNull
  @Override
  public Icon getIcon() {
    return OSGiIcons.ExportPackage;
  }
}
