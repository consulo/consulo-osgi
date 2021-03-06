package consulo.osgi.ide;

import java.awt.event.MouseEvent;
import java.util.List;

import javax.annotation.Nonnull;

import org.osgi.framework.Constants;
import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.DefaultGutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.util.ConstantFunction;
import consulo.osgi.OSGiIcons;
import consulo.osgi.manifest.BundleManifest;
import consulo.osgi.module.OSGiModuleExtensionUtil;
import consulo.osgi.module.extension.OSGiModuleExtension;

/**
 * @author VISTALL
 * @since 18:29/13.05.13
 */
public enum OSGiPsiClassLineMarkerFactory
{
	BUNDLE_ACTIVATOR
			{
				public final GutterIconNavigationHandler<PsiElement> myNavigationHandler = new GutterIconNavigationHandler<PsiElement>()
				{
					@Override
					public void navigate(MouseEvent e, PsiElement elt)
					{
						OSGiModuleExtension facet = OSGiModuleExtensionUtil.findExtension(elt);
						if(facet == null)
						{
							return;
						}
						BundleManifest bundleManifest = facet.getManifest();

						NavigatablePsiElement target = bundleManifest.getNavigateTargetByHeaderName(Constants.BUNDLE_ACTIVATOR);
						if(target == null)
						{
							return;
						}
						PsiElementListNavigator.openTargets(e, new NavigatablePsiElement[]{target}, null, null, new DefaultPsiElementCellRenderer());
					}
				};

				@Override
				public LineMarkerInfo<PsiElement> getLineMarker(@Nonnull PsiIdentifier nameIdentifier, @Nonnull PsiClass psiClass, @Nonnull OSGiModuleExtension facet)
				{
					return OSGiModuleExtensionUtil.isBundleActivator(psiClass) ? new LineMarkerInfo<PsiElement>(nameIdentifier, nameIdentifier.getTextRange(), OSGiIcons.OsgiBundleActivator, Pass
							.LINE_MARKERS, new ConstantFunction<PsiElement, String>("Bundle activator"), myNavigationHandler, GutterIconRenderer.Alignment.LEFT) : null;
				}
			},
	COMPONENT_IMPLEMENTATION
			{
				@Override
				public LineMarkerInfo<PsiElement> getLineMarker(@Nonnull PsiIdentifier nameIdentifier, @Nonnull PsiClass psiClass, @Nonnull OSGiModuleExtension facet)
				{
					final List<NavigatablePsiElement> pairs = OSGiComponentResolver.resolveImpl(psiClass);
					if(pairs.isEmpty())
					{
						return null;
					}

					return new LineMarkerInfo<PsiElement>(nameIdentifier, nameIdentifier.getTextRange(), OSGiIcons.OsgiComponentImplementation, Pass.LINE_MARKERS, new ConstantFunction<PsiElement,
							String>("OSGi component implementation"), new DefaultGutterIconNavigationHandler<PsiElement>(pairs, "Open component descriptors"), GutterIconRenderer.Alignment.LEFT);
				}
			},
	COMPONENT_INTERFACE
			{
				@Override
				public LineMarkerInfo<PsiElement> getLineMarker(@Nonnull PsiIdentifier nameIdentifier, @Nonnull final PsiClass psiClass, @Nonnull OSGiModuleExtension facet)
				{

					final List<NavigatablePsiElement> classes = OSGiComponentResolver.resolveProvide(psiClass);
					if(classes.isEmpty())
					{
						return null;
					}

					return new LineMarkerInfo<PsiElement>(nameIdentifier, nameIdentifier.getTextRange(), OSGiIcons.OsgiComponentInterface, Pass.LINE_MARKERS, new ConstantFunction<PsiElement, String>
							("OSGi component provider"), new GutterIconNavigationHandler<PsiElement>()
					{
						@Override
						public void navigate(MouseEvent e, PsiElement elt)
						{
							final List<NavigatablePsiElement> classes = OSGiComponentResolver.resolveProvide(psiClass);
							if(classes.isEmpty())
							{
								return;
							}
							PsiElementListNavigator.openTargets(e, classes.toArray(new NavigatablePsiElement[classes.size()]), "Navigate to implementation", "Navigate to implementation", new
									DefaultPsiElementCellRenderer());
						}
					}, GutterIconRenderer.Alignment.LEFT);
				}
			};

	public static final OSGiPsiClassLineMarkerFactory[] VALUES = values();

	public abstract LineMarkerInfo<PsiElement> getLineMarker(@Nonnull PsiIdentifier nameIdentifier, @Nonnull PsiClass psiClass, @Nonnull OSGiModuleExtension facet);
}
