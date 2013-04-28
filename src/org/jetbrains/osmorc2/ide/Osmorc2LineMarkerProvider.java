package org.jetbrains.osmorc2.ide;

import aQute.bnd.annotation.component.Component;
import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.util.ConstantFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osmorc2.Osmorc2Icons;
import org.osmorc.BundleManager;
import org.osmorc.facet.OsmorcFacet;
import org.osmorc.facet.OsmorcFacetConfiguration;
import org.osmorc.facet.OsmorcFacetUtil;
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

    final OsmorcFacet osmorcFacet = OsmorcFacetUtil.getInstance(module);
    if (osmorcFacet == null) {
      return;
    }

    if (element instanceof PsiClass) {  // this need check for future usage
      PsiClass psiClass = (PsiClass)element;
      final PsiIdentifier nameIdentifier = psiClass.getNameIdentifier();
      if (nameIdentifier == null) {
        return;
      }

      LineMarkerInfo<PsiElement> temp = createLineMarkerForBundleActivator(psiClass, module, osmorcFacet, nameIdentifier);
      if (temp != null) {
        list.add(temp);
      }

      temp = createLineMarkerForComponentByAnnotation(psiClass, nameIdentifier);
      if (temp != null) {
        list.add(temp);
      }
    }
  }

  private LineMarkerInfo<PsiElement> createLineMarkerForComponentByAnnotation(PsiClass psiClass, PsiIdentifier nameElement) {
    PsiAnnotation annotation = AnnotationUtil.findAnnotation(psiClass, Component.class.getName());
    if (annotation != null) {
      return new LineMarkerInfo<PsiElement>(nameElement, nameElement.getTextRange(), Osmorc2Icons.OsgiComponent,
                                            Pass.UPDATE_OVERRIDEN_MARKERS, new ConstantFunction<PsiElement, String>("OSGi component"),
                                            null, GutterIconRenderer.Alignment.LEFT);
    }
    return null;
  }

  private LineMarkerInfo<PsiElement> createLineMarkerForBundleActivator(PsiClass psiClass,
                                                                        Module module,
                                                                        OsmorcFacet facet,
                                                                        PsiIdentifier nameElement) {
    final String qualifiedName = psiClass.getQualifiedName();
    if (qualifiedName == null) {
      return null;
    }

    boolean needLineMarker = false;
    OsmorcFacetConfiguration configuration = facet.getConfiguration();
    if (configuration.isManifestManuallyEdited()) {
      BundleManager bundleManager = BundleManager.getInstance(psiClass.getProject());

      final BundleManifest manifestByObject = bundleManager.getManifestByObject(module);
      if (manifestByObject == null) {
        return null;
      }
      if (qualifiedName.equals(manifestByObject.getBundleActivator())) {
        needLineMarker = true;
      }
    }
    else {

      if (configuration.getBundleActivator().equals(qualifiedName)) {
        needLineMarker = true;
      }
    }

    return needLineMarker ? new LineMarkerInfo<PsiElement>(nameElement, nameElement.getTextRange(), Osmorc2Icons.OsgiBundleActivator,
                                                           Pass.UPDATE_OVERRIDEN_MARKERS, new ConstantFunction<PsiElement, String>("Bundle activator"),
                                                           null, GutterIconRenderer.Alignment.LEFT) : null;
  }
}
