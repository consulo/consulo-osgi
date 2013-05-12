package org.jetbrains.manifest.bnd.lang;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import org.jetbrains.annotations.NotNull;
import org.osmorc.manifest.lang.ManifestSyntaxHighlighter;

/**
 * @author VISTALL
 * @since 14:10/12.05.13
 */
public class BndSyntaxHighlighter extends ManifestSyntaxHighlighter {
  public BndSyntaxHighlighter() {
    super();
    safeMap(keys, BndTokenType.LINE_COMMENT, DefaultLanguageHighlighterColors.LINE_COMMENT);
  }

  @NotNull
  @Override
  public Lexer getHighlightingLexer() {
    return new BndLexer();
  }
}
