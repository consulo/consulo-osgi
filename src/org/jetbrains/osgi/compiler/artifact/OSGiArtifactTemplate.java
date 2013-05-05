package org.jetbrains.osgi.compiler.artifact;

import com.intellij.packaging.artifacts.ArtifactTemplate;
import com.intellij.packaging.elements.PackagingElementResolvingContext;

/**
 * @author VISTALL
 * @since 19:57/05.05.13
 */
public class OSGiArtifactTemplate extends ArtifactTemplate {
  public OSGiArtifactTemplate(PackagingElementResolvingContext context) {

  }

  @Override
  public String getPresentableName() {
    return "OSGi";
  }
}
