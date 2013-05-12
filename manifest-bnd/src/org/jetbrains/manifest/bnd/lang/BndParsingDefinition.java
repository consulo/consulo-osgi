package org.jetbrains.manifest.bnd.lang;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.osmorc.manifest.lang.ManifestParserDefinition;
import org.osmorc.manifest.lang.psi.impl.ManifestFileImpl;

/**
 * @author VISTALL
 * @since 1:18/12.05.13
 */
public class BndParsingDefinition extends ManifestParserDefinition {
  private static final TokenSet COMMENTS = TokenSet.create(BndTokenType.LINE_COMMENT);

  @Override
  public IFileElementType getFileNodeType() {
    return BndStubElementTypes.FILE;
  }

  @NotNull
  @Override
  public Lexer createLexer(Project project) {
    return new BndLexer();
  }

  @Override
  public PsiFile createFile(FileViewProvider viewProvider) {
    return new ManifestFileImpl(viewProvider, BndLanguage.INSTANCE, BndFileType.INSTANCE);
  }

  @NotNull
  @Override
  public TokenSet getCommentTokens() {
    return COMMENTS;
  }
}
