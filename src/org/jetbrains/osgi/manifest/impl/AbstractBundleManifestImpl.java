package org.jetbrains.osgi.manifest.impl;

import org.apache.felix.framework.util.manifestparser.Capability;
import org.apache.felix.framework.util.manifestparser.ManifestParser;
import org.apache.felix.framework.util.manifestparser.R4Attribute;
import org.apache.felix.framework.util.manifestparser.R4Directive;
import org.apache.felix.moduleloader.ICapability;
import org.apache.felix.moduleloader.IRequirement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osgi.manifest.BundleManifest;
import org.osmorc.manifest.lang.psi.Clause;
import org.osmorc.manifest.lang.psi.Directive;
import org.osmorc.manifest.lang.psi.Header;
import org.osmorc.manifest.lang.valueparser.impl.valueobject.Version;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.osgi.framework.Constants.*;
import static org.osgi.framework.Constants.BUNDLE_CLASSPATH;
import static org.osgi.framework.Constants.FRAGMENT_HOST;

/**
 * @author Robert F. Beeger (robert@beeger.net)
 * @author Jan Thom&auml; (janthomae@janthomae.de)
 * @author VISTALL
 */
public abstract class AbstractBundleManifestImpl implements BundleManifest {
  @Nullable
  protected abstract Header getHeaderByName(@NotNull String heaaderName);

  @Nullable
  protected Object getHeaderValue(@NotNull String headerName) {
    Header headerByName = getHeaderByName(headerName);
    if(headerByName == null) {
      return null;
    }
    return headerByName.getSimpleConvertedValue();
  }

  @NotNull
  public Version getBundleVersion() {
    Version headerValue = (Version)getHeaderValue(BUNDLE_VERSION);
    if (headerValue == null) {
      headerValue = new Version(0, 0, 0, null);
    }
    return headerValue;
  }

  @Nullable
  public String getBundleSymbolicName() {
    return (String)getHeaderValue(BUNDLE_SYMBOLICNAME);
  }

  @Nullable
  public String getBundleActivator() {
    return (String)getHeaderValue(BUNDLE_ACTIVATOR);
  }

  public boolean exportsPackage(@NotNull String packageSpec) {
    Header header = getHeaderByName(EXPORT_PACKAGE);
    if (header == null) {
      return false;
    }

    List<ICapability> capabilities = new ArrayList<ICapability>();
    Clause[] clauses = header.getClauses();
    for (Clause clause : clauses) {
      try {
        capabilities.addAll(Arrays.asList(ManifestParser.parseExportHeader(clause.getClauseText())));
      }
      catch (Exception e) {
        // unparseable header
        return false;
      }
    }

    IRequirement[] requirements;
    try {
      requirements = ManifestParser.parseImportHeader(packageSpec);
    }
    catch (Exception e) {
      // unparseable header
      return false;
    }

    for (IRequirement requirement : requirements) {
      boolean satisfied = false;
      for (ICapability capability : capabilities) {
        if (requirement.isSatisfied(capability)) {
          satisfied = true;
          break;
        }
      }
      if (!satisfied) {
        // at least one requirement is not satisfied by any of the capabilities in this bundle
        return false;
      }
    }

    // all requiremets are satisfied
    return true;
  }

  @NotNull
  @Override
  public List<String> getImports() {
    Header header = getHeaderByName(IMPORT_PACKAGE);
    if (header == null) {
      return Collections.emptyList();
    }
    Clause[] clauses = header.getClauses();
    if(clauses.length == 0) {
      return Collections.emptyList();
    }
    List<String> result = new ArrayList<String>(clauses.length);
    for (Clause clause : clauses) {
      result.add(clause.getClauseText());
    }
    return result;
  }

  @NotNull
  @Override
  public List<String> getExports() {
    Header header = getHeaderByName(EXPORT_PACKAGE);
    if (header == null) {
      return Collections.emptyList();
    }
    Clause[] clauses = header.getClauses();
    if(clauses.length == 0) {
      return Collections.emptyList();
    }
    List<String> result = new ArrayList<String>(clauses.length);
    for (Clause clause : clauses) {
      result.add(clause.getClauseText());
    }
    return result;
  }

  @Override
  @NotNull
  public List<String> getRequiredBundles() {
    Header header = getHeaderByName(REQUIRE_BUNDLE);
    if (header == null) {
      return Collections.emptyList();
    }
    Clause[] clauses = header.getClauses();
    List<String> result = new ArrayList<String>(clauses.length);
    for (Clause clause : clauses) {
      result.add(clause.getClauseText());
    }
    return result;
  }

  @NotNull
  @Override
  public List<String> getReExportedBundles() {
    Header header = getHeaderByName(REQUIRE_BUNDLE);
    if (header == null) {
      return Collections.emptyList();
    }
    Clause[] clauses = header.getClauses();
    List<String> result = new ArrayList<String>(clauses.length);
    for (Clause clause : clauses) {
      Directive visibilityDirectiveName = clause.getDirectiveByName(VISIBILITY_DIRECTIVE);
      if (visibilityDirectiveName != null && VISIBILITY_REEXPORT.equals(visibilityDirectiveName.getValue())) {
        result.add(clause.getClauseText());
      }
    }
    return result;
  }

  @Override
  public boolean isRequiredBundle(@NotNull String bundleSpec) {

    IRequirement[] requirements;
    try {
      requirements = ManifestParser.parseRequireBundleHeader(bundleSpec);
    }
    catch (Exception e) {
      // invalid require spec
      return false;
    }

    // build a capability for this

    String symbolicName = getBundleSymbolicName();
    if (symbolicName == null) {
      return false;
    }
    Version version = getBundleVersion();

    ICapability moduleCapability = new Capability(ICapability.MODULE_NAMESPACE,
                                                  new R4Directive[]{new R4Directive(BUNDLE_SYMBOLICNAME, symbolicName)}, new R4Attribute[]{
      new R4Attribute(BUNDLE_SYMBOLICNAME_ATTRIBUTE, symbolicName, false),
      new R4Attribute(BUNDLE_VERSION_ATTRIBUTE,
                      new org.osgi.framework.Version(version.getMajor(), version.getMinor(), version.getMicro(), version.getQualifier()),
                      false)
    });


    for (IRequirement requirement : requirements) {
      if (!requirement.isSatisfied(moduleCapability)) {
        return false;
      }
    }
    // all requirements are satisfied
    return true;
  }

  @Override
  public boolean reExportsBundle(@NotNull BundleManifest otherBundle) {
    Header header = getHeaderByName(REQUIRE_BUNDLE);
    if (header == null) {
      return false;
    }
    Clause[] clauses = header.getClauses();
    for (Clause clause : clauses) {
      String requireSpec = clause.getClauseText();
      // first check if the clause is set to re-export, if not, we can skip the more expensive checks
      Directive directiveByName = clause.getDirectiveByName(VISIBILITY_DIRECTIVE);
      if (directiveByName == null) {
        continue; // skip to the next require
      }
      if (VISIBILITY_REEXPORT.equals(directiveByName.getValue())) {
        // ok it's a re-export. Now check if the bundle would satisfy the dependency
        if (otherBundle.isRequiredBundle(requireSpec)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public boolean isFragmentBundle() {
    Header header = getHeaderByName(FRAGMENT_HOST);
    return header != null;
  }

  @NotNull
  public List<String> getBundleClassPathEntries() {
    Header header = getHeaderByName(BUNDLE_CLASSPATH);
    if (header == null) {
      return Collections.emptyList();
    }
    Clause[] clauses = header.getClauses();
    List<String> result = new ArrayList<String>(clauses.length);
    for (Clause clause : clauses) {
      result.add(clause.getClauseText());
    }

    return result;
  }


  @Override
  public boolean isFragmentHostFor(@NotNull BundleManifest fragmentBundle) {
    Header header = fragmentBundle.getManifestFile().getHeaderByName(FRAGMENT_HOST);
    if (header == null) {
      return false;
    }

    Clause[] clauses = header.getClauses();
    if (clauses.length != 1) { // bundle should have exactly one clause
      return false;
    }
    Clause clause = clauses[0];
    String hostSpec = clause.getClauseText();
    // they follow the same semantics so i think it is safe to reuse this method here. We do not handle extension bundles at all.
    return isRequiredBundle(hostSpec);
  }
}
