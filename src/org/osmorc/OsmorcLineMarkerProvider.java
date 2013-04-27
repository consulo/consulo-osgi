package org.osmorc;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.util.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osmorc2.Osmorc2Icons;
import org.osmorc.facet.OsmorcFacet;
import org.osmorc.facet.OsmorcFacetConfiguration;

import java.util.Collection;
import java.util.List;

/**
 * @author VISTALL
 * @since 19:08/26.04.13
 */
public class OsmorcLineMarkerProvider implements LineMarkerProvider {
  @Nullable
  @Override
  public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {
    if (element instanceof PsiClass && OsmorcFacet.hasOsmorcFacet(element)) {
      Module module = ModuleUtil.findModuleForPsiElement(element);
      assert module != null;

      final String qualifiedName = ((PsiClass)element).getQualifiedName();
      if (qualifiedName == null) {
        return null;
      }
      OsmorcFacetConfiguration configuration = OsmorcFacet.getInstance(element).getConfiguration();

      if (qualifiedName.equals(configuration.getBundleActivator())) {
        final PsiIdentifier nameIdentifier = ((PsiClass)element).getNameIdentifier();
        if (nameIdentifier == null) {
          return null;
        }
        return new LineMarkerInfo<PsiElement>(nameIdentifier, nameIdentifier.getTextRange(), Osmorc2Icons.LINE_MARKER_ICON,
                                              Pass.UPDATE_ALL, new Function<PsiElement, String>() {
          @Override
          public String fun(PsiElement element) {
            return "Bundle activator";
          }
        }, null, GutterIconRenderer.Alignment.LEFT);
      }

    }
    return null;
  }

  @Override
  public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {
  }
}
