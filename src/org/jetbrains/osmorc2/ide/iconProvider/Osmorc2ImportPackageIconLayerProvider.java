package org.jetbrains.osmorc2.ide.iconProvider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osmorc2.Osmorc2Icons;
import org.osgi.framework.Constants;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 21:14/27.04.13
 */
public class Osmorc2ImportPackageIconLayerProvider extends Osmorc2PackageIconLayerProvider {
  @NotNull
  @Override
  public String getKeyForManifest() {
    return Constants.IMPORT_PACKAGE;
  }

  @Nullable
  @Override
  public String getLayerDescription() {
    return "Imported Package";
  }

  @NotNull
  @Override
  public Icon getIcon() {
    return Osmorc2Icons.ImportPackage;
  }
}
