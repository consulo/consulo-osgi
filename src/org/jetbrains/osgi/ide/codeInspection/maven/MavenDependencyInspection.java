package org.jetbrains.osgi.ide.codeInspection.maven;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.XmlSuppressableInspectionTool;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.XmlElementVisitor;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomManager;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.idea.maven.dom.MavenDomUtil;
import org.jetbrains.idea.maven.dom.model.MavenDomDependency;
import org.jetbrains.idea.maven.model.MavenArtifact;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectsManager;
import org.jetbrains.osgi.facet.OSGiFacetUtil;

import java.util.List;

/**
 * @author VISTALL
 * @since 14:00/07.05.13
 */
public abstract class MavenDependencyInspection extends XmlSuppressableInspectionTool {
  @Nls
  @NotNull
  public abstract String getDisplayName();

  @NotNull
  public String getShortName() {
    return getClass().getSimpleName();
  }

  @Nls
  @NotNull
  public String getGroupDisplayName() {
    return "OSGi";
  }

  @Override
  public boolean isEnabledByDefault() {
    return true;
  }

  @NotNull
  @Override
  public final PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder problemsHolder, boolean b) {
    return new XmlElementVisitor() {
      public void visitXmlTag(XmlTag xmltag) {
        // suppress inspection for projects not having an OSGi context.
        if (OSGiFacetUtil.findFacet(xmltag) == null) {
          return;
        }
        // get the dependency
        MavenDomDependency dependency = getDependency(xmltag);
        if (dependency != null) {
          String scope = dependency.getScope().getStringValue();
          if ("test".equals(scope)) {
            // don't test this for "test" dependencies...
            return;
          }
          Module module = ModuleUtil.findModuleForPsiElement(xmltag);

          assert module != null;

          // get the projects manager for this dependency
          MavenProjectsManager manager = MavenProjectsManager.getInstance(xmltag.getProject());
          MavenProject project = manager.findProject(module);

          if (project == null) {
            return;
          }

          final List<MavenArtifact> dependencies =
            project.findDependencies(dependency.getGroupId().getStringValue(), dependency.getArtifactId().getStringValue());

          MavenArtifact jarArtifact = null;
          for (MavenArtifact artifact : dependencies) {
            if (artifact.getClassifier() == null) {
              jarArtifact = artifact;
              break;
            }
          }

          if(jarArtifact == null) {
            return;
          }
          registerProblems(module, xmltag, jarArtifact, problemsHolder);
        }
      }
    };
  }

  public abstract void registerProblems(Module module, XmlTag xmlTag, MavenArtifact artifact, ProblemsHolder problemsHolder);

  public static MavenDomDependency getDependency(XmlTag xmltag) {
    // avoid going through the dom for each and every tag.
    if (!"dependency".equals(xmltag.getName())) {
      return null;
    }
    DomElement dom = DomManager.getDomManager(xmltag.getProject()).getDomElement(xmltag);
    if (dom != null) {
      return dom.getParentOfType(MavenDomDependency.class, false);
    }
    return null;
  }


  @Override
  public ProblemDescriptor[] checkFile(@NotNull PsiFile psiFile, @NotNull InspectionManager inspectionManager, boolean b) {
    if (!MavenDomUtil.isMavenFile(psiFile) || OSGiFacetUtil.findFacet(psiFile) == null) {
      return ProblemDescriptor.EMPTY_ARRAY;
    }
    else {
      return super.checkFile(psiFile, inspectionManager, b);
    }
  }
}
