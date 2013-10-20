package org.jetbrains.osgi.compiler.artifact;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.packaging.artifacts.Artifact;
import com.intellij.packaging.impl.artifacts.ArtifactUtil;
import com.intellij.packaging.impl.artifacts.DefaultPackagingElementResolvingContext;
import com.intellij.packaging.impl.elements.ModuleOutputPackagingElement;
import com.intellij.packaging.impl.elements.moduleContent.ProductionModuleOutputElementType;
import com.intellij.util.Processor;

/**
 * @author VISTALL
 * @since 13:55/06.05.13
 */
public class OSGiArtifactUtil {
  @NotNull
  public static Module[] collectModules(@NotNull Project project, @NotNull Artifact artifact) {
    final DefaultPackagingElementResolvingContext context = new DefaultPackagingElementResolvingContext(project);
    final List<Module> list = new ArrayList<Module>();
    ArtifactUtil
      .processPackagingElements(artifact, ProductionModuleOutputElementType.getInstance(), new Processor<ModuleOutputPackagingElement>() {
        @Override
        public boolean process(ModuleOutputPackagingElement moduleOutputPackagingElement) {
          final Module module = moduleOutputPackagingElement.findModule(context);
          if (module != null) {
            list.add(module);
          }
          return true;
        }
      }, context, true);
    return list.isEmpty() ? Module.EMPTY_ARRAY : list.toArray(new Module[list.size()]);
  }
}
