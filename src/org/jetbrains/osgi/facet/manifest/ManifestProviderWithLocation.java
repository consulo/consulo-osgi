package org.jetbrains.osgi.facet.manifest;

import com.intellij.openapi.components.PathMacroManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.vcsUtil.VcsUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osgi.facet.manifest.impl.SimpleConfigurableWithLocation;
import org.jetbrains.osgi.manifest.BundleManifest;
import org.jetbrains.osgi.manifest.impl.BundleManifestImpl;
import org.jetbrains.osgi.module.extension.OSGiModuleExtension;
import org.osmorc.manifest.lang.psi.ManifestFile;

/**
 * @author VISTALL
 * @since 14:58/30.04.13
 */
public abstract class ManifestProviderWithLocation extends ManifestProvider {

  private CachedValue<BundleManifest> myCachedValue;

  private class OurCachedValueProvider implements CachedValueProvider<BundleManifest> {
    private final Project myProject;

    private OurCachedValueProvider(Project project) {
      myProject = project;
    }

    @Nullable
    @Override
    public Result<BundleManifest> compute() {
      VirtualFile virtualFile = VcsUtil.getVirtualFile(myLocation);
      if(virtualFile == null) {
        return null;
      }

      PsiManager manager = PsiManager.getInstance(myProject);

      PsiFile file = manager.findFile(virtualFile);
      if(!(file instanceof ManifestFile)) {
        return null;
      }
      return Result.<BundleManifest>create(new BundleManifestImpl((ManifestFile) file), PsiModificationTracker.MODIFICATION_COUNT);
    }
  }

  @NotNull
  protected String myLocation;

  protected ManifestProviderWithLocation(@NotNull String defaultValue) {
    myLocation = defaultValue;
  }

  @Override
  public void readExternal(Element element) throws InvalidDataException {
    setLocation(element.getAttributeValue("location"));
  }

  @Override
  public void writeExternal(Element element) throws WriteExternalException {
    element.setAttribute("location", myLocation);
  }

  @NotNull
  @Override
  public ManifestProviderConfigurable createConfigurable(Module module) {
    return new SimpleConfigurableWithLocation(getHeaderName(), this, module);
  }

  @NotNull
  public abstract String getHeaderName();

  @Nullable
  @Override
  protected BundleManifest getBundleManifestImpl(OSGiModuleExtension facet) {
    if(myCachedValue == null) {
      myCachedValue = CachedValuesManager.getManager(facet.getModule().getProject()).createCachedValue(new OurCachedValueProvider(facet.getModule().getProject()));
    }
    return myCachedValue.getValue();
  }

  @NotNull
  public String getLocation() {
    return myLocation;
  }

  public void setLocation(@NotNull String location) {
    myLocation = StringUtil.notNullize(location);
  }

  @Override
  public void expandPaths(PathMacroManager pathMacroManager) {
    myLocation = pathMacroManager.expandPath(myLocation);
  }
}
