package org.jetbrains.osgi.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.facet.FacetTypeId;
import com.intellij.openapi.components.PathMacroManager;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osgi.OSGiConstants;
import org.jetbrains.osgi.OSGiIcons;
import org.jetbrains.osgi.compiler.ManifestProvider;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 17:34/29.04.13
 */
public class OSGiFacetType extends FacetType<OSGiFacet, OSGiFacetConfiguration> {
  public static final FacetTypeId<OSGiFacet> ID = new FacetTypeId<OSGiFacet>("OSGi");

  public OSGiFacetType() {
    super(ID, "OSGi", "OSGi", null);
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return OSGiIcons.FacetType;
  }

  @Override
  public OSGiFacetConfiguration createDefaultConfiguration() {
    OSGiFacetConfiguration facetConfiguration = new OSGiFacetConfiguration();
    facetConfiguration.setOsgiInfLocation(OSGiConstants.DEFAULT_OSGI_INF_ROOT_LOCATION);
    facetConfiguration.setMetaInfLocation(OSGiConstants.DEFAULT_META_INF_ROOT_LOCATION);
    facetConfiguration.setActiveManifestProvider(facetConfiguration.getManifestProviders()[0]);
    return facetConfiguration;
  }

  @Override
  public OSGiFacet createFacet(@NotNull Module module,
                               String name,
                               @NotNull OSGiFacetConfiguration configuration,
                               @Nullable Facet underlyingFacet) {

    OSGiFacet facet = new OSGiFacet(this, module, name, configuration, underlyingFacet);

    PathMacroManager pathMacroManager = PathMacroManager.getInstance(module);

    configuration.setOsgiInfLocation(pathMacroManager.expandPath(OSGiConstants.DEFAULT_OSGI_INF_ROOT_LOCATION));
    configuration.setMetaInfLocation(pathMacroManager.expandPath(OSGiConstants.DEFAULT_META_INF_ROOT_LOCATION));
    for(ManifestProvider manifestProvider : configuration.getManifestProviders()) {
      manifestProvider.expandPaths(pathMacroManager);
    }
    return facet;
  }

  @Override
  public boolean isSuitableModuleType(ModuleType moduleType) {
    return moduleType instanceof JavaModuleType;
  }
}
