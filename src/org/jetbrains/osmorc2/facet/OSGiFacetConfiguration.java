package org.jetbrains.osmorc2.facet;

import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PathMacroManager;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.osmorc2.compiler.ManifestProvider;
import org.jetbrains.osmorc2.compiler.ManifestProviderEP;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 17:35/29.04.13
 */
public class OSGiFacetConfiguration implements FacetConfiguration {
  private final ManifestProvider[] myManifestProviders;

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
    return new FacetEditorTab[0];
  }

  @Override
  public void readExternal(Element element) throws InvalidDataException {
    PathMacroManager.getInstance(ApplicationManager.getApplication()).expandPaths(element);

    List<Element> elementChildren = element.getChildren();
    ManifestProvider[] manifestProviders = myManifestProviders;
    for(Element childElement : elementChildren) {
      if("manifest-provider".equals(childElement.getName())) {
        String className = childElement.getAttributeValue("class");
        boolean active = Boolean.parseBoolean(childElement.getAttributeValue("active"));

        ManifestProvider manifestProvider = null;
        for(ManifestProvider temp : manifestProviders) {
          if(className.equals(temp.getClass().getName())) {
            manifestProvider = temp;
          }
        }

        if(manifestProvider == null) {
          continue;
        }

        manifestProvider.setActive(active);
        manifestProvider.readExternal(childElement);
      }
    }

    checkActive();
  }

  @Override
  public void writeExternal(Element element) throws WriteExternalException {
    for(ManifestProvider provider : myManifestProviders) {
      Element providerElement = new Element("manifest-provider");
      providerElement.setAttribute("active", String.valueOf(provider.isActive()));
      providerElement.setAttribute("class", provider.getClass().getName());

      provider.writeExternal(providerElement);

      element.addContent(providerElement);
    }
  }

  public void checkActive() {
    boolean noActive = true;
    for(ManifestProvider manifestProvider : myManifestProviders) {
      if(manifestProvider.isActive()) {
        noActive = false;
        break;
      }
    }

    if(noActive) {
      myManifestProviders[0].setActive(true);
    }
  }

  public ManifestProvider[] getManifestProviders() {
    return myManifestProviders;
  }
}
