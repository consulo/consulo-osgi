package org.jetbrains.manifest.bnd.lang;

import com.intellij.lang.Commenter;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 14:27/12.05.13
 */
public class BndCommenter implements Commenter {
  @Nullable
  @Override
  public String getLineCommentPrefix() {
    return "#";
  }

  @Nullable
  @Override
  public String getBlockCommentPrefix() {
    return null;
  }

  @Nullable
  @Override
  public String getBlockCommentSuffix() {
    return null;
  }

  @Nullable
  @Override
  public String getCommentedBlockCommentPrefix() {
    return null;
  }

  @Nullable
  @Override
  public String getCommentedBlockCommentSuffix() {
    return null;
  }
}