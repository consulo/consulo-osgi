package org.osmorc.frameworkintegration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FrameworkIntegratorUtil {

  @Nullable
  public static FrameworkIntegrator findIntegratorByName(@Nonnull final String name) {
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
  public static FrameworkIntegrator findIntegratorByInstanceDefinition(@Nonnull final FrameworkInstanceDefinition frameworkInstanceDefinition) {
    return findIntegratorByName(frameworkInstanceDefinition.getFrameworkIntegratorName());
  }
}
