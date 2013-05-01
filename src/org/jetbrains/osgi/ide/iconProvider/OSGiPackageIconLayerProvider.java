package org.jetbrains.osgi.ide.iconProvider;

import com.intellij.ide.IconLayerProvider;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osgi.facet.OSGiFacet;
import org.jetbrains.osgi.facet.OSGiFacetUtil;
import org.jetbrains.osgi.manifest.BundleManifest;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 21:07/27.04.13
 */
public abstract class OSGiPackageIconLayerProvider implements IconLayerProvider {
  protected abstract boolean isApplicable(@NotNull String qName, @NotNull BundleManifest bundleManifest);

  @NotNull
  public abstract Icon getIcon();

  @Nullable
  @Override
  public Icon getLayerIcon(@NotNull Iconable element, boolean isLocked) {
    if (element instanceof PsiDirectory) {
      final PsiPackage dirPackage = JavaDirectoryService.getInstance().getPackage((PsiDirectory)element);
      if (dirPackage == null) {
        return null;
      }
      Module module = ModuleUtil.findModuleForPsiElement((PsiDirectory)element);
      if (module == null) {
        return null;
      }
      final OSGiFacet facet = OSGiFacetUtil.findFacet(module);
      if (facet == null) {
        return null;
      }

      final String qualifiedName = dirPackage.getQualifiedName();
      if (qualifiedName.isEmpty()) {
        return null;
      }

      BundleManifest bundleManifest = facet.getConfiguration().getActiveManifestProvider().getBundleManifest(dirPackage.getProject());
      if (isApplicable(qualifiedName, bundleManifest)) {
        return getIcon();
      }
    }
    return null;
  }
}
