package aQute.bnd.service;

import aQute.bnd.build.Project;

import java.util.Set;

public interface DependencyContributor {
  void addDependencies(Project project, Set<String> dependencies);
}
