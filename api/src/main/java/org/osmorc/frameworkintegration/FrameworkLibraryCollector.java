package org.osmorc.frameworkintegration;

import com.intellij.openapi.vfs.VirtualFile;
import javax.annotation.Nonnull;

import java.util.Collection;

/**
 * A library collector interface for collecting the libraries of a framework.
 */
public interface FrameworkLibraryCollector {

  /**
   * Collects the framework libraries.
   *
   * @param sourceFinder        the source finder which can be used to retrieve sources for libraries
   * @param directoriesWithJars a list of directories containing jar files. each jar file will be collected.s
   */
  void collectFrameworkLibraries(@Nonnull FrameworkInstanceLibrarySourceFinder sourceFinder,
                                 @Nonnull Collection<VirtualFile> directoriesWithJars);
}
