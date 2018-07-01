package aQute.bnd.service;

import aQute.bnd.build.Container;
import aQute.bnd.build.Project;

import java.io.File;
import java.util.Collection;

public interface Compiler {
  boolean compile(Project project, Collection<File> sources, Collection<Container> buildpath, File bin) throws Exception;
}
