package org.jetbrains.manifest.bnd.lang;

import org.osmorc.manifest.lang.ManifestTokenType;

/**
 * @author VISTALL
 * @since 1:23/12.05.13
 */
public interface BndTokenTypes {
  public static ManifestTokenType SHARP = new ManifestTokenType("SHARP", BndLanguage.INSTANCE);

  public static ManifestTokenType LINE_COMMENT = new ManifestTokenType("LINE_COMMENT", BndLanguage.INSTANCE);
}
