package org.osmorc.frameworkintegration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FrameworkIntegratorUtil {

  @Nullable
  public static FrameworkIntegrator findIntegratorByName(@NotNull final String name) {
    FrameworkIntegrator result = null;

    for (FrameworkIntegrator frameworkIntegrator : FrameworkIntegrator.EP_NAME.getExtensions()) {
      if (frameworkIntegrator.getDisplayName().equals(name)) {
        result = frameworkIntegrator;
        break;
      }
    }

    return result;
  }

  @Nullable
  public static FrameworkIntegrator findIntegratorByInstanceDefinition(@NotNull final FrameworkInstanceDefinition frameworkInstanceDefinition) {
    return findIntegratorByName(frameworkInstanceDefinition.getFrameworkIntegratorName());
  }
}
