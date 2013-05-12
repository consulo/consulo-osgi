package org.jetbrains.manifest.bnd.lang;

import com.intellij.lang.Language;
import org.osmorc.manifest.lang.ManifestLanguage;

/**
 * @author VISTALL
 * @since 1:19/12.05.13
 */
public class BndLanguage extends Language {
  public static final Language INSTANCE = new BndLanguage();

  public BndLanguage() {
    super(ManifestLanguage.INSTANCE, "BND");
  }
}
