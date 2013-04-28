package org.jetbrains.osmorc2.ide;

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
import org.osmorc.BundleManager;
import org.osmorc.facet.OsmorcFacet;
import org.osmorc.facet.OsmorcFacetConfiguration;
import org.osmorc.manifest.BundleManifest;

import java.util.Collection;
import java.util.List;

/**
 * @author VISTALL
 * @since 19:08/26.04.13
 */
public class Osmorc2LineMarkerProvider implements LineMarkerProvider {
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
      if(configuration.isManifestManuallyEdited()) {
        BundleManager bundleManager = BundleManager.getInstance(element.getProject());

        final BundleManifest manifestByObject = bundleManager.getManifestByObject(module);
        if(manifestByObject == null) {
          return null;
        }
        if(qualifiedName.equals(manifestByObject.getBundleActivator())) {
          return create((PsiClass)element);
        }
      }
      else {

        if(configuration.getBundleActivator().equals(qualifiedName)) {
          return create((PsiClass)element);
        }
      }
    }
    return null;
  }

  private LineMarkerInfo<PsiElement> create(PsiClass element) {
    final PsiIdentifier nameIdentifier = element.getNameIdentifier();
    if (nameIdentifier == null) {
      return null;
    }
    return new LineMarkerInfo<PsiElement>(nameIdentifier, nameIdentifier.getTextRange(), Osmorc2Icons.BundleActivator,
                                          Pass.UPDATE_ALL, new Function<PsiElement, String>() {
      @Override
      public String fun(PsiElement element) {
        return "Bundle activator";
      }
    }, null, GutterIconRenderer.Alignment.LEFT);
  }

  @Override
  public void collectSlowLineMarkers(@NotNull List< PsiElement > elements, @NotNull Collection<LineMarkerInfo> result) {
  }
}
