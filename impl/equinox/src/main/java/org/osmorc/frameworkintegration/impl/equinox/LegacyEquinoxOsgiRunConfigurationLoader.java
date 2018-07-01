/*
 * Copyright (c) 2007-2009, Osmorc Development Team
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright notice, this list
 *       of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice, this
 *       list of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *     * Neither the name of 'Osmorc Development Team' nor the names of its contributors may be
 *       used to endorse or promote products derived from this software without specific
 *       prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.osmorc.frameworkintegration.impl.equinox;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osmorc.frameworkintegration.*;
import org.osmorc.run.LegacyOsgiRunConfigurationLoader;
import org.osmorc.run.OsgiRunConfiguration;
import org.osmorc.run.ui.BundleType;
import org.osmorc.run.ui.SelectedBundle;
import org.osmorc.settings.ApplicationSettings;
import org.osmorc.settings.ProjectSettings;

import java.util.Collection;
import java.util.List;

/**
 * Loads the legacy Eclipse Equinox Run Configurations as OSGi Run Configurations.
 *
 * @author Robert F. Beeger (robert@beeger.net)
 */
@SuppressWarnings({"MethodMayBeStatic", "UnusedDeclaration"})
public class LegacyEquinoxOsgiRunConfigurationLoader implements LegacyOsgiRunConfigurationLoader {

  public void finishAfterModulesAreAvailable(OsgiRunConfiguration osgiRunConfiguration) {
    List<SelectedBundle> bundlesToDeploy = osgiRunConfiguration.getBundlesToDeploy();
    addModuleBundles(bundlesToDeploy, osgiRunConfiguration.getProject());

    FrameworkInstanceDefinition frameworkInstanceDefinition = getFrameworkInstance(osgiRunConfiguration.getProject());

    if (frameworkInstanceDefinition != null) {
      osgiRunConfiguration.setInstanceToUse(frameworkInstanceDefinition);

      addFrameworkBundle(bundlesToDeploy, frameworkInstanceDefinition);
    }
  }

  @Nullable
  private FrameworkInstanceDefinition getFrameworkInstance(Project project) {
    ApplicationSettings applicationSettings = ServiceManager.getService(ApplicationSettings.class);
    ProjectSettings projectSettings = ServiceManager.getService(project, ProjectSettings.class);
    return applicationSettings.getFrameworkInstance(projectSettings.getFrameworkInstanceName());
  }

  private void addModuleBundles(List<SelectedBundle> bundlesToDeploy, Project project) {
    /*Module[] modules = ModuleManager.getInstance(project).getModules();
    for (Module module : modules) {
      if (OsmorcFacetUtil.hasOsmorcFacet(module)) {
        SelectedBundle bundle = new SelectedBundle(module.getName(), null, BundleType.Module);
        bundle.setStartLevel(4);
        bundlesToDeploy.add(bundle);
      }
    }*/
  }

  private void addFrameworkBundle(final List<SelectedBundle> bundlesToDeploy,
                                  @NotNull FrameworkInstanceDefinition frameworkInstanceDefinition) {
    FrameworkIntegrator frameworkIntegrator = FrameworkIntegratorUtil.findIntegratorByInstanceDefinition(frameworkInstanceDefinition);
    FrameworkInstanceManager frameworkInstanceManager = frameworkIntegrator.getInstanceManager();

    frameworkInstanceManager.collectLibraries(frameworkInstanceDefinition, new JarFileLibraryCollector() {
      @Override
      protected void collectFrameworkJars(@NotNull Collection<VirtualFile> jarFiles,
                                          @NotNull FrameworkInstanceLibrarySourceFinder sourceFinder) {
        for (VirtualFile jarFile : jarFiles) {
          String url = jarFile.getUrl();
          if (url.contains("org.eclipse.equinox.common_")) {
            SelectedBundle bundle = createSelectedFrameworkBundle(jarFile.getPath());
            if (bundle != null) {
              bundle.setStartLevel(2);
              bundle.setStartAfterInstallation(true);
              bundlesToDeploy.add(bundle);
            }
          }
          else if (url.contains("org.eclipse.update.configurator_")) {
            SelectedBundle bundle = createSelectedFrameworkBundle(jarFile.getPath());
            if (bundle != null) {
              bundle.setStartLevel(3);
              bundle.setStartAfterInstallation(true);
              bundlesToDeploy.add(bundle);
            }
          }
          else if (url.contains("org.eclipse.core.runtime_")) {
            SelectedBundle bundle = createSelectedFrameworkBundle(jarFile.getPath());
            if (bundle != null) {
              bundle.setStartLevel(4);
              bundle.setStartAfterInstallation(true);
              bundlesToDeploy.add(bundle);
            }
          }
        }
      }
    });
  }

  @Nullable
  private SelectedBundle createSelectedFrameworkBundle(String url) {
    String bundleName = CachingBundleInfoProvider.getBundleSymbolicName(url);
    SelectedBundle bundle = null;
    if (bundleName != null) {
      String bundleVersion = CachingBundleInfoProvider.getBundleVersions(url);
      bundle = new SelectedBundle(bundleName + " - " + bundleVersion, url, BundleType.FrameworkBundle);
    }
    return bundle;
  }
}
