package org.jetbrains.osgi.ide;

import aQute.bnd.annotation.component.Component;
import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.*;
import com.intellij.util.ConstantFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.osgi.OSGiIcons;
import org.jetbrains.osgi.facet.OSGiFacet;
import org.jetbrains.osgi.facet.OSGiFacetUtil;
import org.jetbrains.osgi.manifest.BundleManifest;
import org.osgi.framework.Constants;

import java.awt.event.MouseEvent;

/**
 * @author VISTALL
 * @since 18:29/13.05.13
 */
public enum OSGiPsiClassLineMarkerFactory {
  BUNDLE_ACTIVATOR {
    public final GutterIconNavigationHandler<PsiElement> myNavigationHandler = new GutterIconNavigationHandler<PsiElement>() {
      @Override
      public void navigate(MouseEvent e, PsiElement elt) {
        OSGiFacet facet = OSGiFacetUtil.findFacet(elt);
        if (facet == null) {
          return;
        }
        BundleManifest bundleManifest = facet.getManifest();

        NavigatablePsiElement target = bundleManifest.getNavigateTargetByHeaderName(Constants.BUNDLE_ACTIVATOR);
        if (target == null) {
          return;
        }
        PsiElementListNavigator.openTargets(e, new NavigatablePsiElement[]{target}, null, null, new DefaultPsiElementCellRenderer());
      }
    };

    @Override
    public LineMarkerInfo<PsiElement> getLineMarker(@NotNull PsiIdentifier nameIdentifier, @NotNull PsiClass psiClass, @NotNull OSGiFacet facet) {
      return OSGiFacetUtil.isBundleActivator(psiClass) ? new LineMarkerInfo<PsiElement>(nameIdentifier, nameIdentifier.getTextRange(), OSGiIcons.OsgiBundleActivator,
                                                                                        Pass.UPDATE_OVERRIDEN_MARKERS,
                                                                                        new ConstantFunction<PsiElement, String>("Bundle activator"),
                                                                                        myNavigationHandler, GutterIconRenderer.Alignment.LEFT) : null;
    }
  },
  COMPONENT_IMPLEMENTATION_BY_ANNOTATION {
    @Override
    public LineMarkerInfo<PsiElement> getLineMarker(@NotNull PsiIdentifier nameIdentifier, @NotNull PsiClass psiClass, @NotNull OSGiFacet facet) {
      PsiAnnotation annotation = AnnotationUtil.findAnnotation(psiClass, Component.class.getName());
      if (annotation != null) {
        return new LineMarkerInfo<PsiElement>(nameIdentifier, nameIdentifier.getTextRange(), OSGiIcons.OsgiComponentImplementation,
                                              Pass.UPDATE_OVERRIDEN_MARKERS, new ConstantFunction<PsiElement, String>("OSGi component"), null,
                                              GutterIconRenderer.Alignment.LEFT);
      }
      return null;
    }
  },
  COMPONENT_INTERFACE_BY_ANNOTATION {
    @Override
    public LineMarkerInfo<PsiElement> getLineMarker(@NotNull PsiIdentifier nameIdentifier, @NotNull PsiClass psiClass, @NotNull OSGiFacet facet) {
      return null;
    }
  },
  COMPONENT_IMPLEMENTATION_BY_XML {
    @Override
    public LineMarkerInfo<PsiElement> getLineMarker(@NotNull PsiIdentifier nameIdentifier, @NotNull PsiClass psiClass, @NotNull OSGiFacet facet) {
      return null;
    }
  },
  COMPONENT_INTERFACE_BY_XML {
    @Override
    public LineMarkerInfo<PsiElement> getLineMarker(@NotNull PsiIdentifier nameIdentifier, @NotNull PsiClass psiClass, @NotNull OSGiFacet facet) {
      return null;
    }
  };

  public static final OSGiPsiClassLineMarkerFactory[] VALUES = values();

  public abstract LineMarkerInfo<PsiElement> getLineMarker(@NotNull PsiIdentifier nameIdentifier, @NotNull PsiClass psiClass, @NotNull OSGiFacet facet);
}
