package org.jetbrains.osgi.ide;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator;
import com.intellij.codeInsight.navigation.ClassImplementationsSearch;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.util.ConstantFunction;
import com.intellij.util.Processor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.osgi.OSGiConstants;
import org.jetbrains.osgi.OSGiIcons;
import org.jetbrains.osgi.facet.OSGiFacet;
import org.jetbrains.osgi.facet.OSGiFacetUtil;
import org.jetbrains.osgi.manifest.BundleManifest;
import org.osgi.framework.Constants;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

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
      return OSGiFacetUtil.isBundleActivator(psiClass) ? new LineMarkerInfo<PsiElement>(nameIdentifier, nameIdentifier.getTextRange(),
                                                                                        OSGiIcons.OsgiBundleActivator, Pass.UPDATE_OVERRIDEN_MARKERS,
                                                                                        new ConstantFunction<PsiElement, String>("Bundle activator"),
                                                                                        myNavigationHandler, GutterIconRenderer.Alignment.LEFT) : null;
    }
  },
  COMPONENT_IMPLEMENTATION_BY_ANNOTATION {
    @Override
    public LineMarkerInfo<PsiElement> getLineMarker(@NotNull PsiIdentifier nameIdentifier, @NotNull PsiClass psiClass, @NotNull OSGiFacet facet) {
      PsiAnnotation annotation = AnnotationUtil.findAnnotation(psiClass, OSGiConstants.COMPONENT_ANNOTATION);
      if (annotation != null) {
        return new LineMarkerInfo<PsiElement>(nameIdentifier, nameIdentifier.getTextRange(), OSGiIcons.OsgiComponentImplementation,
                                              Pass.UPDATE_OVERRIDEN_MARKERS, new ConstantFunction<PsiElement, String>("OSGi component implementation"), null,
                                              GutterIconRenderer.Alignment.LEFT);
      }
      return null;
    }
  },
  COMPONENT_INTERFACE_BY_ANNOTATION {
    @Override
    public LineMarkerInfo<PsiElement> getLineMarker(@NotNull PsiIdentifier nameIdentifier, @NotNull final PsiClass psiClass, @NotNull OSGiFacet facet) {

      final List<PsiClass> classes = collectImplementations(psiClass);
      if (classes.isEmpty()) {
        return null;
      }

      return new LineMarkerInfo<PsiElement>(nameIdentifier, nameIdentifier.getTextRange(), OSGiIcons.OsgiComponentInterface, Pass.UPDATE_OVERRIDEN_MARKERS,
                                            new ConstantFunction<PsiElement, String>("OSGi component provider"), new GutterIconNavigationHandler<PsiElement>() {
        @Override
        public void navigate(MouseEvent e, PsiElement elt) {
          final List<PsiClass> classes = collectImplementations(psiClass);
          if(classes.isEmpty()) {
            return;
          }
          PsiElementListNavigator.openTargets(e, classes.toArray(new NavigatablePsiElement[classes.size()]), "Navigate to implementation", "Navigate to implementation", new DefaultPsiElementCellRenderer());
        }
      }, GutterIconRenderer.Alignment.LEFT);
    }

    private List<PsiClass> collectImplementations(@NotNull final PsiClass owner) {
      final List<PsiClass> list = new ArrayList<PsiClass>(2);
      if (CommonClassNames.JAVA_LANG_OBJECT.equals(owner.getQualifiedName())) {
        return null;
      }

      ClassImplementationsSearch.processImplementations(owner, new Processor<PsiClass>() {
        @Override
        public boolean process(PsiClass iterationClass) {
          PsiAnnotation annotation = AnnotationUtil.findAnnotation(iterationClass, OSGiConstants.COMPONENT_ANNOTATION);
          if (annotation == null) {
            return true;
          }

          final PsiAnnotationMemberValue provide = annotation.findDeclaredAttributeValue("provide");
          if (provide == null) {
            for (PsiClass interfacePsiClass : iterationClass.getInterfaces()) {
              if (interfacePsiClass.isEquivalentTo(owner)) {
                list.add(iterationClass);
              }
            }
          }
          else if (provide instanceof PsiArrayInitializerMemberValue) {
            for (PsiAnnotationMemberValue initializer : ((PsiArrayInitializerMemberValue)provide).getInitializers()) {
              if (initializer instanceof PsiClassObjectAccessExpression) {
                final PsiType type = ((PsiClassObjectAccessExpression)initializer).getOperand().getType();
                final PsiClass psiClassOfType = PsiTypesUtil.getPsiClass(type);
                if (owner.isEquivalentTo(psiClassOfType)) {
                  list.add(iterationClass);
                }
              }
            }
          }
          else if (provide instanceof PsiClassObjectAccessExpression) {
            final PsiType type = ((PsiClassObjectAccessExpression)provide).getOperand().getType();
            final PsiClass psiClassOfType = PsiTypesUtil.getPsiClass(type);
            if (owner.isEquivalentTo(psiClassOfType)) {
              list.add(iterationClass);
            }
          }
          return true;
        }
      });
      return list;
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
