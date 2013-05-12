package org.jetbrains.manifest.bnd.lang;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 1:23/12.05.13
 */
public class BndTokenType extends IElementType {
  public static BndTokenType SHARP = new BndTokenType("SHARP");

  public static BndTokenType LINE_COMMENT = new BndTokenType("LINE_COMMENT");

  public BndTokenType(@NotNull @NonNls String debugName) {
    super(debugName, BndLanguage.INSTANCE);
  }

  @Override
  public String toString() {
    return "BndTokenType: " + super.toString();
  }
}
