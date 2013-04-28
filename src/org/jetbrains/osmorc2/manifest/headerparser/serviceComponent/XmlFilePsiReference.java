package org.jetbrains.osmorc2.manifest.headerparser.serviceComponent;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;
import com.intellij.vcsUtil.VcsUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osmorc2.serviceComponent.dom.ComponentElement;

import java.util.*;

/**
 * @author VISTALL
 * @since 14:49/28.04.13
 */
public class XmlFilePsiReference extends PsiReferenceBase<PsiElement> {

  private final String myFilePath;

  public XmlFilePsiReference(PsiElement element, String filePath) {
    super(element, new TextRange(element.getStartOffsetInParent(), element.getStartOffsetInParent() + element.getTextLength()), false);
    myFilePath = filePath;
  }

  @Nullable
  @Override
  public PsiElement resolve() {
    return getComponents().get(myFilePath);
  }

  @NotNull
  @Override
  public Object[] getVariants() {
    final Map<String, XmlFile> components = getComponents();
    return components.keySet().toArray(new String[components.size()]);
  }

  private Map<String, XmlFile> getComponents() {
    final Module moduleForPsiElement = ModuleUtil.findModuleForPsiElement(myElement);
    if (moduleForPsiElement == null) {
      return Collections.emptyMap();
    }

    ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(moduleForPsiElement);

    List<VirtualFile> files = new ArrayList<VirtualFile>();
    for (VirtualFile virtualFile : moduleRootManager.getSourceRoots()) {
      final VirtualFile child = virtualFile.findChild("OSGI-INF");
      if (child != null) {
        VcsUtil.collectFiles(child, files, true, false);
      }
    }

    final DomManager domManager = DomManager.getDomManager(getElement().getProject());
    final PsiManager manager = PsiManager.getInstance(getElement().getProject());
    final ProjectFileIndex projectFileIndex = ProjectFileIndex.SERVICE.getInstance(getElement().getProject());

    Map<String, XmlFile> map = new HashMap<String, XmlFile>();
    for (VirtualFile file : files) {
      PsiFile psiFile = manager.findFile(file);
      if (psiFile instanceof XmlFile) {
        final DomFileElement<ComponentElement> fileElement = domManager.getFileElement((XmlFile)psiFile, ComponentElement.class);
        if(fileElement != null) {

          final VirtualFile sourceRootForFile = projectFileIndex.getSourceRootForFile(file);
          final String filePath = file.getPath();
          final String parentPath = sourceRootForFile.getPath();

          final String shortPath = filePath.substring(parentPath.length() + 1, filePath.length());
          map.put(shortPath, (XmlFile)psiFile);
        }
      }
    }

    return map;
  }
}
