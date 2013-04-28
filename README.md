### Roadmap

 * Bnd file support (parsing, highlight, etc)
 * Service-Component xml file support(DOM) (IDEA-71982/IDEA-65524)
 * Rewrite manifest holding - make different impl for different Facet settings. It ill remove this hardcode
 * Remove dependent 'apache-felix'
 * support IDEA-75423

```java
  OsmorcFacetConfiguration configuration = OsmorcFacet.getInstance(element).getConfiguration();
  if(configuration.isManifestManuallyEdited()) {
    BundleManager bundleManager = BundleManager.getInstance(element.getProject());

    final BundleManifest manifestByObject = bundleManager.getManifestByObject(module);
    if(manifestByObject == null) {
      return null;
    }
    if(qualifiedName.equals(manifestByObject.getBundleActivator())) {
      return create((PsiClass)element);
    }
  }
  else {

    if(configuration.getBundleActivator().equals(qualifiedName)) {
      return create((PsiClass)element);
    }
  }
```

### Issues

  Fixed

    * IDEA-80352 / IDEA-105427

  Actual:

    * IDEA-77441
    * IDEA-75423

  Obsolote:

    * IDEA-77773 / IDEA-99995