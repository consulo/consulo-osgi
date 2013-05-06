package org.jetbrains.osgi.compiler.artifact;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.roots.ui.configuration.ChooseModulesDialog;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.packaging.artifacts.ArtifactTemplate;
import com.intellij.packaging.elements.CompositePackagingElement;
import com.intellij.packaging.elements.PackagingElementFactory;
import com.intellij.packaging.elements.PackagingElementResolvingContext;
import com.intellij.packaging.impl.artifacts.ArtifactUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osgi.OSGiConstants;
import org.jetbrains.osgi.facet.OSGiFacetUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 19:57/05.05.13
 */
public class OSGiArtifactTemplate extends ArtifactTemplate {
  private PackagingElementResolvingContext myContext;

  public OSGiArtifactTemplate(PackagingElementResolvingContext context) {
    myContext = context;
  }

  @Nullable
  @Override
  public NewArtifactConfiguration createArtifact() {
    List<Module> modules = new ArrayList<Module>();
    for (Module module : ModuleManager.getInstance(myContext.getProject()).getModules()) {
      if (OSGiFacetUtil.findFacet(module) != null) {
        modules.add(module);
      }
    }
    ChooseModulesDialog dialog =
      new ChooseModulesDialog(myContext.getProject(), modules, "Choose Module", "Choose module for artifact creation");
    dialog.setSingleSelectionMode();
    final List<Module> selectedModules = dialog.showAndGetResult();
    if (selectedModules.size() != 1) {
      return null;
    }
    return doCreateArtifact(selectedModules.get(0));
  }

  private NewArtifactConfiguration doCreateArtifact(Module module) {
    final String name = module.getName();

    final PackagingElementFactory factory = PackagingElementFactory.getInstance();
    final CompositePackagingElement<?> archive = factory.createArchive(ArtifactUtil.suggestArtifactFileName(name) + ".jar");

    archive.addOrFindChild(factory.createModuleOutput(module));

    VirtualFile osgi = getVirtualFileBasedOnModule(module, OSGiConstants.OSGI_INFO_ROOT);
    if(osgi != null) {
      final CompositePackagingElement<?> osgiRoot = archive.addOrFindChild(factory.createDirectory(OSGiConstants.OSGI_INFO_ROOT));
      osgiRoot.addOrFindChild(factory.createDirectoryCopyWithParentDirectories(osgi.getPath(), "/"));
    }
    VirtualFile meta = getVirtualFileBasedOnModule(module, OSGiConstants.META_INFO_ROOT);
    if(meta != null) {
      final CompositePackagingElement<?> metaRoot = archive.addOrFindChild(factory.createDirectory(OSGiConstants.META_INFO_ROOT));
      metaRoot.addOrFindChild(factory.createDirectoryCopyWithParentDirectories(meta.getPath(), "/"));
    }

    return new NewArtifactConfiguration(archive, archive.getName(), OSGiArtifactType.getInstance());
  }

  @Nullable
  public VirtualFile getVirtualFileBasedOnModule(@NotNull Module module, @NotNull String root) {
    final VirtualFile moduleFile = module.getModuleFile();
    if(moduleFile == null) {
      return null;
    }
    final VirtualFile parent = moduleFile.getParent();
    if(parent == null) {
      return null;
    }
    return parent.findChild(root);
  }

  @Override
  public String getPresentableName() {
    return "By module";
  }
}
