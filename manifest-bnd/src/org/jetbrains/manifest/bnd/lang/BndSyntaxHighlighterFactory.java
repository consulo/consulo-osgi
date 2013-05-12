package org.jetbrains.manifest.bnd.lang;

import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 14:56/12.05.13
 */
public class BndSyntaxHighlighterFactory extends SingleLazyInstanceSyntaxHighlighterFactory {
  @NotNull
  @Override
  protected SyntaxHighlighter createHighlighter() {
    return new BndSyntaxHighlighter();
  }
}
