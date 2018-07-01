package aQute.bnd.service.action;

import aQute.bnd.build.Project;

public interface Action {
  void execute(Project project, String action) throws Exception;
}
