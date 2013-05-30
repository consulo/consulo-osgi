package org.jetbrains.osgi.facet.manifest.impl;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.vcsUtil.VcsUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osgi.OSGiConstants;
import org.jetbrains.osgi.facet.manifest.ManifestProvider;
import org.jetbrains.osgi.facet.manifest.ManifestProviderConfigurable;
import org.jetbrains.osgi.manifest.BundleManifest;
import org.jetbrains.osgi.manifest.impl.BundleManifestImpl;
import org.jetbrains.osgi.module.extension.OSGiModuleExtension;
import org.osmorc.manifest.lang.psi.ManifestFile;

import javax.swing.JComponent;

/**
 * @author VISTALL
 * @since 14:06/29.04.13
 */
public class UseExistingManifestManifestProvider extends ManifestProvider {
  @NotNull
  @Override
  public ManifestProviderConfigurable createConfigurable(Module module) {
    return new ManifestProviderConfigurable<UseExistingManifestManifestProvider>(this) {
      @NotNull
      @Override
      public String getHeaderName() {
        return "Use existing manifest";
      }

      @Nullable
      @Override
      public JComponent createComponent() {
        return null;
      }

      @Override
      public boolean isModified() {
        return false;
      }

      @Override
      public void apply() throws ConfigurationException {
      }

      @Override
      public void reset() {
      }

      @Override
      public void disposeUIResources() {
      }
    };
  }

  @Nullable
  @Override
  protected BundleManifest getBundleManifestImpl(OSGiModuleExtension facet) {
    final VirtualFile virtualFile = VcsUtil.getVirtualFile(facet.getMETAInf() + "/" + OSGiConstants.MANIFEST_NAME);
    if (virtualFile == null) {
      return null;
    }
    PsiManager manager = PsiManager.getInstance(facet.getModule().getProject());

    PsiFile file = manager.findFile(virtualFile);
    if (!(file instanceof ManifestFile)) {
      return null;
    }
    return new BundleManifestImpl((ManifestFile)file);
  }
}
