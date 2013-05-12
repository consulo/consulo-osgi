package org.jetbrains.manifest.bnd.lang;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.manifest.bnd.BndIcons;

import javax.swing.Icon;

/**
 * @author VISTALL
 * @since 1:31/12.05.13
 */
public class BndFileType extends LanguageFileType {
  public static final BndFileType INSTANCE = new BndFileType();

  private BndFileType() {
    super(BndLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public String getName() {
    return "BND";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "Bnd files";
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return "bnd";
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return BndIcons.FILE_ICON;
  }
}
