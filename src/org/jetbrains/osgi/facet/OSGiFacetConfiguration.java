package org.jetbrains.osgi.facet;

import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PathMacroManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.packaging.artifacts.Artifact;
import com.intellij.packaging.artifacts.ArtifactManager;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.osgi.compiler.ManifestProvider;
import org.jetbrains.osgi.compiler.ManifestProviderEP;
import org.jetbrains.osgi.facet.ui.GeneralFacetEditorTab;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 17:35/29.04.13
 */
public class OSGiFacetConfiguration implements FacetConfiguration {
  private final ManifestProvider[] myManifestProviders;
  private ManifestProvider myActiveManifestProvider;

  private String myOsgiInfLocation;
  private String myMetaInfLocation;

  private String myArtifactName;

  public OSGiFacetConfiguration() {
    ManifestProviderEP[] extensions = ManifestProviderEP.EP_NAME.getExtensions();
    List<ManifestProvider> providers = new ArrayList<ManifestProvider>(extensions.length);

    for (ManifestProviderEP extension : extensions) {
      ManifestProvider providerFor = extension.createProvider();
      if (providerFor != null) {
        providers.add(providerFor);
      }
    }
    myManifestProviders = providers.toArray(new ManifestProvider[providers.size()]);
    assert myManifestProviders.length != 0;
  }

  @Override
  public FacetEditorTab[] createEditorTabs(FacetEditorContext editorContext, FacetValidatorsManager validatorsManager) {
    return new FacetEditorTab[]{new GeneralFacetEditorTab(this, validatorsManager, editorContext.getModule(), (OSGiFacet) editorContext.getFacet())};
  }

  @Override
  public void readExternal(Element element) throws InvalidDataException {
    PathMacroManager.getInstance(ApplicationManager.getApplication()).expandPaths(element);

    setOsgiInfLocation(element.getAttributeValue("osgi-inf-path"));
    setMetaInfLocation(element.getAttributeValue("meta-inf-path"));

    myArtifactName = element.getAttributeValue("artifact-name", (String)null);

    List<Element> elementChildren = element.getChildren();
    ManifestProvider[] manifestProviders = myManifestProviders;
    for (Element childElement : elementChildren) {
      if ("manifest-provider".equals(childElement.getName())) {
        String className = childElement.getAttributeValue("class");
        boolean active = Boolean.parseBoolean(childElement.getAttributeValue("active"));

        ManifestProvider manifestProvider = null;
        for (ManifestProvider temp : manifestProviders) {
          if (className.equals(temp.getClass().getName())) {
            manifestProvider = temp;
          }
        }

        if (manifestProvider == null) {
          continue;
        }

        if (active && myActiveManifestProvider == null) {
          myActiveManifestProvider = manifestProvider;
        }
        manifestProvider.readExternal(childElement);
      }
    }

    validateAndCreate();
  }

  public Artifact getArtifact(@NotNull Project project) {
    return myArtifactName == null ? null : ArtifactManager.getInstance(project).findArtifact(myArtifactName);
  }

  @Override
  public void writeExternal(Element element) throws WriteExternalException {
    element.setAttribute("osgi-inf-path", myOsgiInfLocation);
    element.setAttribute("meth-inf-path", myMetaInfLocation);
    if (myArtifactName != null) {
      element.setAttribute("artifact-name", myArtifactName);
    }

    for (ManifestProvider provider : myManifestProviders) {
      Element providerElement = new Element("manifest-provider");
      providerElement.setAttribute("active", String.valueOf(myActiveManifestProvider == provider));
      providerElement.setAttribute("class", provider.getClass().getName());

      provider.writeExternal(providerElement);

      element.addContent(providerElement);
    }

    // PathMacroManager.getInstance(ApplicationManager.getApplication()).collapsePaths(element);
  }

  public void validateAndCreate() {
    if (myActiveManifestProvider == null) {
      myActiveManifestProvider = myManifestProviders[0];
    }

    FileUtilRt.createParentDirs(new File(myOsgiInfLocation));
    myActiveManifestProvider.validateAndCreate();
  }

  public ManifestProvider[] getManifestProviders() {
    return myManifestProviders;
  }

  public boolean isActive(ManifestProvider manifestProvider) {
    return myActiveManifestProvider == manifestProvider;
  }

  public void setActiveManifestProvider(ManifestProvider activeManifestProvider) {
    myActiveManifestProvider = activeManifestProvider;
  }

  public ManifestProvider getActiveManifestProvider() {
    return myActiveManifestProvider;
  }

  public String getOsgiInfLocation() {
    return StringUtil.notNullize(myOsgiInfLocation);
  }

  public void setOsgiInfLocation(String osgiInfLocation) {
    myOsgiInfLocation = osgiInfLocation;
  }

  public String getArtifactName() {
    return myArtifactName;
  }

  public void setArtifactName(String artifactName) {
    myArtifactName = artifactName;
  }

  @NotNull
  public String getMetaInfLocation() {
    return StringUtil.notNullize(myMetaInfLocation);
  }

  public void setMetaInfLocation(String metaInfLocation) {
    myMetaInfLocation = metaInfLocation;
  }
}
