package aQute.bnd.service;

import aQute.bnd.build.Project;
import aQute.lib.osgi.Jar;

/**
 * Deploy this artifact to maven.
 */
public interface Deploy {
  boolean deploy(Project project, Jar jar) throws Exception;
}
