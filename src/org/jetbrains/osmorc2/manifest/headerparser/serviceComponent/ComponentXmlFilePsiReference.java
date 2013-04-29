package org.jetbrains.osmorc2.manifest.headerparser.serviceComponent;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
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
import org.jetbrains.osmorc2.OsgiConstants;
import org.jetbrains.osmorc2.serviceComponent.dom.TComponent;
import org.osmorc.facet.OsmorcFacetUtil;

import java.util.*;

/**
 * @author VISTALL
 * @since 14:49/28.04.13
 */
public class ComponentXmlFilePsiReference extends PsiReferenceBase<PsiElement> {

  private final String myFilePath;

  public ComponentXmlFilePsiReference(PsiElement element, String filePath) {
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
    final VirtualFile osgiInfRoot = OsmorcFacetUtil.getOsgiInfRoot(moduleForPsiElement);
    if(osgiInfRoot == null) {
      return null;
    }

    List<VirtualFile> files = new ArrayList<VirtualFile>();
    VcsUtil.collectFiles(osgiInfRoot, files, true, false);

    final DomManager domManager = DomManager.getDomManager(getElement().getProject());
    final PsiManager manager = PsiManager.getInstance(getElement().getProject());

    Map<String, XmlFile> map = new HashMap<String, XmlFile>();
    for (VirtualFile file : files) {
      PsiFile psiFile = manager.findFile(file);
      if (psiFile instanceof XmlFile) {
        final DomFileElement<TComponent> fileElement = domManager.getFileElement((XmlFile)psiFile, TComponent.class);
        if (fileElement != null) {
          final String filePath = file.getPath();
          final String shortPath = filePath.substring(osgiInfRoot.getPath().length() - OsgiConstants.OSGI_INFO_ROOT.length(), filePath.length());
          map.put(shortPath, (XmlFile)psiFile);
        }
      }
    }

    return map;
  }
}
