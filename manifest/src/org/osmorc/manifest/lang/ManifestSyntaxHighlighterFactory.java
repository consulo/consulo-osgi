package org.osmorc.manifest.lang;

import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 14:55/12.05.13
 */
public class ManifestSyntaxHighlighterFactory extends SingleLazyInstanceSyntaxHighlighterFactory {
  @NotNull
  @Override
  protected SyntaxHighlighter createHighlighter() {
    return new ManifestSyntaxHighlighter();
  }
}
