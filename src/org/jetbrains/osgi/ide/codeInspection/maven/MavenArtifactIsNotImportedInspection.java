package org.jetbrains.osgi.ide.codeInspection.maven;

import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.Processor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.idea.maven.model.MavenArtifact;
import org.jetbrains.osgi.facet.OSGiFacetUtil;
import org.jetbrains.osgi.manifest.BundleManifest;
import org.osmorc.BundleManager;

/**
 * @author VISTALL
 * @since 13:59/07.05.13
 */
public class MavenArtifactIsNotImportedInspection extends MavenDependencyInspection {
  @Nls
  @NotNull
  @Override
  public String getDisplayName() {
    return "Artifact is not imported in MANIFEST.MF";
  }

  @Override
  public void registerProblems(Module module, XmlTag xmlTag, MavenArtifact artifact, ProblemsHolder problemsHolder) {
    final Library library = findLibrary(module, artifact);
    if(library == null) {
      return;
    }

    final BundleManifest bundleManifest = OSGiFacetUtil.findFacet(module).getConfiguration().getBundleManifest(module.getProject());

    BundleManager bundleManager = BundleManager.getInstance(module.getProject());

    final BundleManifest manifestByObject = bundleManager.getManifestByObject(library);
    if(manifestByObject == null) {
      return;
    }


    for(String importPackage : bundleManifest.getImports()) {
      if(manifestByObject.exportsPackage(importPackage)) {
        return;
      }
    }

    problemsHolder.registerProblem(xmlTag, getDisplayName(), new LocalQuickFixAndIntentionActionOnPsiElement(xmlTag) {
      @Override
      public void invoke(@NotNull Project project,
                         @NotNull PsiFile file,
                         @Nullable("is null when called from inspection") Editor editor,
                         @NotNull PsiElement startElement,
                         @NotNull PsiElement endElement) {
        startElement.delete();
      }

      @NotNull
      @Override
      public String getText() {
        return "Remove artifact";
      }

      @NotNull
      @Override
      public String getFamilyName() {
        return "OSGi";
      }
    });
  }

  private static Library findLibrary(@NotNull Module module, @NotNull final MavenArtifact artifact) {
    final String name = artifact.getLibraryName();
    final Ref<Library> result = Ref.create(null);
    ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(module);
    moduleRootManager.orderEntries().forEachLibrary(new Processor<Library>() {
      @Override
      public boolean process(Library library) {
        if (name.equals(library.getName())) {
          result.set(library);
        }
        return true;
      }
    });
    return result.get();
  }
}
