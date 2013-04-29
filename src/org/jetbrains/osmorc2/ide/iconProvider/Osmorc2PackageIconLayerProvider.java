package org.jetbrains.osmorc2.ide.iconProvider;

import com.intellij.ide.IconLayerProvider;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import com.intellij.util.ArrayUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osmorc.BundleManager;
import org.osmorc.facet.OsmorcFacet;
import org.osmorc.facet.OsmorcFacetConfiguration;
import org.osmorc.facet.OsmorcFacetUtil;
import org.jetbrains.osmorc2.manifest.BundleManifest;

import javax.swing.*;
import java.util.List;
import java.util.Map;

/**
 * @author VISTALL
 * @since 21:07/27.04.13
 */
public abstract class Osmorc2PackageIconLayerProvider implements IconLayerProvider {
  private static final String ALL = "*";

  @NotNull
  public abstract String getKeyForManifest();

  @NotNull
  public abstract Icon getIcon();

  @Nullable
  @Override
  public Icon getLayerIcon(@NotNull Iconable element, boolean isLocked) {
    if(element instanceof PsiDirectory) {
      final PsiPackage dirPackage = JavaDirectoryService.getInstance().getPackage((PsiDirectory)element);
      if(dirPackage == null) {
        return null;
      }
      Module module = ModuleUtil.findModuleForPsiElement((PsiDirectory)element);
      if(module == null) {
        return null;
      }
      final OsmorcFacet instance = OsmorcFacetUtil.getInstance(module);
      if(instance == null) {
        return null;
      }
      OsmorcFacetConfiguration configuration = instance.getConfiguration();
      final String qualifiedName = dirPackage.getQualifiedName();
      if(qualifiedName.isEmpty()) {
        return null;
      }
      if (configuration.isManifestManuallyEdited()) {
        BundleManager bundleManager = BundleManager.getInstance(module.getProject());
        BundleManifest manifest = bundleManager.getManifestByObject(module);
        if(manifest != null) {
          final List<String> exports = manifest.getManifestFile().getValuesByKey(getKeyForManifest());
          if(exports.contains(ALL) || exports.contains(qualifiedName)) {
            return getIcon();
          }
        }
      }
      else {
        final Map<String,String> additionalPropertiesAsMap = configuration.getAdditionalPropertiesAsMap();

        String value = additionalPropertiesAsMap.get(getKeyForManifest());
        if(value == null || ALL.equals(value)) {
          return getIcon();
        }
        else {
          final String[] split = value.split(",");
          if(ArrayUtil.contains(qualifiedName, split)) {
            return getIcon();
          }
        }
      }
    }
    return null;
  }
}
