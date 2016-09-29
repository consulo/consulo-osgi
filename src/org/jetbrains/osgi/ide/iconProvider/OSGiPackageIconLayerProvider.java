package org.jetbrains.osgi.ide.iconProvider;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.osgi.facet.OSGiFacetUtil;
import org.jetbrains.osgi.manifest.BundleManifest;
import org.jetbrains.osgi.module.extension.OSGiModuleExtension;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaPackage;
import consulo.ide.IconDescriptor;
import consulo.ide.IconDescriptorUpdater;

/**
 * @author VISTALL
 * @since 21:07/27.04.13
 */
public abstract class OSGiPackageIconLayerProvider implements IconDescriptorUpdater
{
	protected abstract boolean isApplicable(@NotNull String qName, @NotNull BundleManifest bundleManifest);

	@NotNull
	public abstract Icon getIcon();

	@Override
	public void updateIcon(@NotNull IconDescriptor iconDescriptor, @NotNull PsiElement element, int flags)
	{
		if(element instanceof PsiDirectory)
		{
			final PsiJavaPackage dirPackage = JavaDirectoryService.getInstance().getPackage((PsiDirectory) element);
			if(dirPackage == null)
			{
				return;
			}
			Module module = ModuleUtil.findModuleForPsiElement(element);
			if(module == null)
			{
				return;
			}
			final OSGiModuleExtension facet = OSGiFacetUtil.findFacet(module);
			if(facet == null)
			{
				return;
			}

			final String qualifiedName = dirPackage.getQualifiedName();
			if(qualifiedName.isEmpty())
			{
				return;
			}

			BundleManifest bundleManifest = facet.getManifest();
			if(isApplicable(qualifiedName, bundleManifest))
			{
				iconDescriptor.setRightIcon(getIcon());
			}
		}
	}
}
