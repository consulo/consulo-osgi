package aQute.bnd.service;

import aQute.lib.osgi.Jar;

import java.io.File;

public interface RepositoryListenerPlugin {

  /**
   * Called when a bundle is added to a repository.
   *
   * @param repository
   * @param jar
   * @param file
   */
  void bundleAdded(RepositoryPlugin repository, Jar jar, File file);
}
