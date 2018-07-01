package aQute.bnd.service;

import java.io.File;

public interface Refreshable {
  boolean refresh();

  File getRoot();
}
