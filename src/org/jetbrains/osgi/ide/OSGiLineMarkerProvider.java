package org.jetbrains.osgi.ide;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osgi.facet.OSGiFacetUtil;
import org.jetbrains.osgi.module.extension.OSGiModuleExtension;

import java.util.Collection;
import java.util.List;

/**
 * @author VISTALL
 * @since 19:08/26.04.13
 */
public class OSGiLineMarkerProvider implements LineMarkerProvider {
  @Nullable
  @Override
  public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {
    return null;
  }

  public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {
    for (PsiElement e : elements) {
      createLineMarkers(result, e);
    }
  }

  private void createLineMarkers(Collection<LineMarkerInfo> list, PsiElement element) {
    if (!(element instanceof PsiClass)) { //fast hack - we need only psi class
      return;
    }

    Module module = ModuleUtil.findModuleForPsiElement(element);
    if (module == null) {
      return;
    }

    final OSGiModuleExtension osGiFacet = OSGiFacetUtil.findFacet(module);
    if (osGiFacet == null) {
      return;
    }

    if (element instanceof PsiClass) {  // this need check for future usage
      PsiClass psiClass = (PsiClass)element;
      final PsiIdentifier nameIdentifier = psiClass.getNameIdentifier();
      if (nameIdentifier == null) {
        return;
      }

      for(OSGiPsiClassLineMarkerFactory factory : OSGiPsiClassLineMarkerFactory.VALUES) {
        final LineMarkerInfo<PsiElement> lineMarker = factory.getLineMarker(nameIdentifier, psiClass, osGiFacet);
        if(lineMarker != null) {
          list.add(lineMarker);
        }
      }
    }
  }
}
