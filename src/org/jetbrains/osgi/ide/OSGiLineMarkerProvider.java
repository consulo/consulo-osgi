package org.jetbrains.osgi.ide;

import aQute.bnd.annotation.component.Component;
import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.*;
import com.intellij.util.ConstantFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osgi.OSGiIcons;
import org.jetbrains.osgi.facet.OSGiFacet;
import org.jetbrains.osgi.facet.OSGiFacetUtil;
import org.jetbrains.osgi.manifest.BundleManifest;
import org.osgi.framework.Constants;

import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;

/**
 * @author VISTALL
 * @since 19:08/26.04.13
 */
public class OSGiLineMarkerProvider implements LineMarkerProvider {

  public static final GutterIconNavigationHandler<PsiElement> BUNDLE_ACTIVATOR_HANDLER = new GutterIconNavigationHandler<PsiElement>() {
    @Override
    public void navigate(MouseEvent e, PsiElement elt) {
      OSGiFacet facet = OSGiFacetUtil.findFacet(elt);
      if (facet == null) {
        return;
      }
      BundleManifest bundleManifest = facet.getConfiguration().getActiveManifestProvider().getBundleManifest(elt.getProject());

      NavigatablePsiElement target = bundleManifest.getNavigateTargetByHeaderName(Constants.BUNDLE_ACTIVATOR);
      if (target == null) {
        return;
      }
      PsiElementListNavigator.openTargets(e, new NavigatablePsiElement[]{target}, null, null, new DefaultPsiElementCellRenderer());
    }
  };

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

    final OSGiFacet osGiFacet = OSGiFacetUtil.findFacet(module);
    if (osGiFacet == null) {
      return;
    }

    if (element instanceof PsiClass) {  // this need check for future usage
      PsiClass psiClass = (PsiClass)element;
      final PsiIdentifier nameIdentifier = psiClass.getNameIdentifier();
      if (nameIdentifier == null) {
        return;
      }

      LineMarkerInfo<PsiElement> temp = createLineMarkerForBundleActivator(psiClass, nameIdentifier);
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
      return new LineMarkerInfo<PsiElement>(nameElement, nameElement.getTextRange(), OSGiIcons.OsgiComponent, Pass.UPDATE_OVERRIDEN_MARKERS,
                                            new ConstantFunction<PsiElement, String>("OSGi component"), null,
                                            GutterIconRenderer.Alignment.LEFT);
    }
    return null;
  }

  private LineMarkerInfo<PsiElement> createLineMarkerForBundleActivator(PsiClass psiClass, PsiIdentifier nameElement) {

    return OSGiFacetUtil.isBundleActivator(psiClass) ? new LineMarkerInfo<PsiElement>(nameElement, nameElement.getTextRange(),
                                                                                      OSGiIcons.OsgiBundleActivator,
                                                                                      Pass.UPDATE_OVERRIDEN_MARKERS,
                                                                                      new ConstantFunction<PsiElement, String>(
                                                                                        "Bundle activator"), BUNDLE_ACTIVATOR_HANDLER,
                                                                                      GutterIconRenderer.Alignment.LEFT) : null;
  }
}
