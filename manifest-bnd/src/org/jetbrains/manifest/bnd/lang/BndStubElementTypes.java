package org.jetbrains.manifest.bnd.lang;

import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.IStubFileElementType;

/**
 * @author VISTALL
 * @since 14:32/12.05.13
 */
public interface BndStubElementTypes {
  IFileElementType FILE = new IStubFileElementType("BndFile", BndLanguage.INSTANCE) {
    @Override
    public int getStubVersion() {
      return 3;
    }
  };
}
