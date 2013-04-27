package org.osmorc.make;

import com.intellij.openapi.compiler.CompileContext;
import org.jetbrains.annotations.NotNull;

/**
 * A wrapper around SpringSource's Bundlor tool.
 *
 * @author <a href="janthomae@janthomae.de">Jan Thom&auml;</a>
 * @version $Id:$
 *
 * TODO [VISTALL] split
 */
public class BundlorWrapper {
  public boolean wrapModule(@NotNull CompileContext compileContext,
                            @NotNull String inputJar,
                            @NotNull String outputJar,
                            @NotNull String manifestTemplateFile) {

   /* // IDEA-71307, add maven project properties to scope, in case the project is maven based.
    MavenProjectsManager mpm = MavenProjectsManager.getInstance(compileContext.getProject());

    Properties props = new Properties();

    if (mpm.isMavenizedProject()) {
      Module[] affectedModules = compileContext.getCompileScope().getAffectedModules();
      for (Module affectedModule : affectedModules) {
        MavenProject project = mpm.findProject(affectedModule);
        if (project != null) {
          props.putAll(project.getProperties());
        }
      }
    }

    PropertiesSource ps = new PropertiesPropertiesSource(props);

    ManifestWriter manifestWriter = new StandardManifestWriterFactory().create(inputJar, outputJar);

    ManifestGenerator manifestGenerator = new StandardManifestGenerator(DefaultManifestGeneratorContributorsFactory.create(ps));
    ClassPathFactory cpf = new StandardClassPathFactory();
    ClassPath classPath = cpf.create(inputJar);

    StandardManifestValidator manifestValidator = new StandardManifestValidator(DefaultManifestValidatorContributorsFactory.create());

    ManifestContents manifest =
      manifestGenerator.generate(new StandardManifestTemplateFactory().create(manifestTemplateFile, null, null, null), classPath);

    try {
      manifestWriter.write(manifest);
    }
    catch (Exception e) {
      compileContext.addMessage(CompilerMessageCategory.ERROR, "Error writing manifest: " + e.getMessage(), null, 0, 0);
      return false;
    }
    finally {
      manifestWriter.close();
    }

    final List<String> warningsList = manifestValidator.validate(manifest);
    for (String s : warningsList) {
      compileContext.addMessage(CompilerMessageCategory.WARNING, s, VfsUtil.pathToUrl(manifestTemplateFile), 0, 0);
    }
    return true;  */
    return false; //TODO [VISTALL] move
  }
}
