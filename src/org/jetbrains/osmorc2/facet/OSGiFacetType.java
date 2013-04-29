package org.jetbrains.osmorc2.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.facet.FacetTypeId;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osmorc2.Osmorc2Icons;

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
    return Osmorc2Icons.FacetType;
  }

  @Override
  public OSGiFacetConfiguration createDefaultConfiguration() {
    OSGiFacetConfiguration facetConfiguration = new OSGiFacetConfiguration();
    facetConfiguration.checkActive();
    return facetConfiguration;
  }

  @Override
  public OSGiFacet createFacet(@NotNull Module module,
                               String name,
                               @NotNull OSGiFacetConfiguration configuration,
                               @Nullable Facet underlyingFacet) {
    return new OSGiFacet(this,module, name, configuration, underlyingFacet);
  }

  @Override
  public boolean isSuitableModuleType(ModuleType moduleType) {
    return moduleType instanceof JavaModuleType;
  }
}
