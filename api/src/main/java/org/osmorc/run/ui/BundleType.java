package org.osmorc.run.ui;

/**
 * The type of a selected bundle,
 */
public enum BundleType {
  Artifact(true),

  /**
   * The selected bundle is an existing bundle that is part of the OSGi framework (e.g. the Knopflerfish Desktop
   * bundle).
   */
  FrameworkBundle(true),
  /**
   * The selected bundle is a library used in this project, that should be started. This is rarely used, except for
   * Libraries that are meant to be used as bundles (such as Spring-DM).
   */
  StartableLibrary(true),
  /**
   * The selected bundle is a plain library that should be installed only.
   */
  PlainLibrary(false);

  private final boolean defaultStartAfterInstallation;

  BundleType(boolean startAfterInstallation) {
    defaultStartAfterInstallation = startAfterInstallation;
  }

  /**
   * @return true if bundles of this type should by default be started after installation, false otherwise.
   */
  public boolean isDefaultStartAfterInstallation() {
    return defaultStartAfterInstallation;
  }
}
