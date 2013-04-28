package org.jetbrains.osmorc2.manifest.headerparser;

import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.osmorc2.manifest.headerparser.serviceComponent.XmlFilePsiReference;
import org.osmorc.manifest.lang.headerparser.impl.AbstractHeaderParserImpl;
import org.osmorc.manifest.lang.psi.Clause;
import org.osmorc.manifest.lang.psi.HeaderValuePart;

/**
 * @author VISTALL
 * @since 14:46/28.04.13
 */
public class ServiceComponentHeaderParser extends AbstractHeaderParserImpl {
  public PsiReference[] getReferences(@NotNull HeaderValuePart headerValuePart) {
    if (headerValuePart.getParent() instanceof Clause) {
      return new PsiReference[] {new XmlFilePsiReference(headerValuePart, headerValuePart.getUnwrappedText()) };
    }
    return PsiReference.EMPTY_ARRAY;
  }

  @Override
  public boolean isSimpleHeader() {
    return false;
  }
}
