package org.jetbrains.osgi.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.openapi.module.Module;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.osgi.facet.manifest.ManifestProvider;
import org.jetbrains.osgi.manifest.BundleManifest;

/**
 * @author VISTALL
 * @since 17:34/29.04.13
 */
public class OSGiFacet extends Facet<OSGiFacetConfiguration> {

  public OSGiFacet(@NotNull FacetType facetType,
                   @NotNull Module module,
                   @NotNull String name,
                   @NotNull OSGiFacetConfiguration configuration,
                   Facet underlyingFacet) {
    super(facetType, module, name, configuration, underlyingFacet);
  }

  @NotNull
  public BundleManifest getManifest() {
    final ManifestProvider activeManifestProvider = getConfiguration().getActiveManifestProvider();
    return activeManifestProvider.getBundleManifest(this);
  }
}
