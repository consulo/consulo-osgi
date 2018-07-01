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

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.VirtualFile;
import javax.annotation.Nonnull;
import org.osmorc.frameworkintegration.*;
import org.osmorc.run.ui.BundleType;
import org.osmorc.run.ui.SelectedBundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Robert F. Beeger (robert@beeger.net)
 */
@SuppressWarnings({"ComponentNotRegistered"})
class AdaptToRunWithUpdateConfiguratorAction extends BundleSelectionAction {
  private static final String ORG_ECLIPSE_EQUINOX_COMMON_URL = "org.eclipse.equinox.common_";
  private static final String ORG_ECLIPSE_UPDATE_CONFIGURATOR_URL = "org.eclipse.update.configurator_";
  private static final String ORG_ECLIPSE_CORE_RUNTIME_URL = "org.eclipse.core.runtime_";

  public AdaptToRunWithUpdateConfiguratorAction() {
    super("Adapt to Run With Update Configurator");
  }

  public void actionPerformed(AnActionEvent e) {

    final List<String> necessaryFrameworkBundleURLs = new ArrayList<String>();
    necessaryFrameworkBundleURLs.add(ORG_ECLIPSE_EQUINOX_COMMON_URL);
    necessaryFrameworkBundleURLs.add(ORG_ECLIPSE_UPDATE_CONFIGURATOR_URL);
    necessaryFrameworkBundleURLs.add(ORG_ECLIPSE_CORE_RUNTIME_URL);

    Collection<SelectedBundle> currentlySelectedBundles = new ArrayList<SelectedBundle>(getContext().getCurrentlySelectedBundles());
    for (SelectedBundle selectedBundle : currentlySelectedBundles) {
      if (selectedBundle.getBundleType() == BundleType.FrameworkBundle) {
        String url = selectedBundle.getBundlePath();
        boolean necessaryFrameworkBundleFound = false;
        if (url != null) {
          for (Iterator<String> iterator = necessaryFrameworkBundleURLs.iterator(); iterator.hasNext(); ) {
            String necessaryFrameworkBundleURL = iterator.next();
            if (url.contains(necessaryFrameworkBundleURL)) {
              adaptBundle(selectedBundle);
              necessaryFrameworkBundleFound = true;
              iterator.remove();
              break;
            }
          }
        }
        if (!necessaryFrameworkBundleFound) {
          getContext().removeBundle(selectedBundle);
        }
      }
    }

    if (necessaryFrameworkBundleURLs.size() > 0) {
      FrameworkInstanceDefinition instance = getContext().getUsedFrameworkInstance();
      assert instance != null;
      FrameworkIntegrator frameworkIntegrator = FrameworkIntegratorUtil.findIntegratorByInstanceDefinition(instance);
      frameworkIntegrator.getInstanceManager().collectLibraries(instance, new JarFileLibraryCollector() {
        @Override
        protected void collectFrameworkJars(@Nonnull Collection<VirtualFile> jarFiles,
                                            @Nonnull FrameworkInstanceLibrarySourceFinder sourceFinder) {
          for (VirtualFile jarFile : jarFiles) {
            String url = jarFile.getUrl();
            for (Iterator<String> iterator = necessaryFrameworkBundleURLs.iterator(); iterator.hasNext(); ) {
              String necessaryFrameworkBundleURL = iterator.next();
              if (url.contains(necessaryFrameworkBundleURL)) {
                SelectedBundle bundle = createSelectedFrameworkBundle(jarFile.getPath());
                adaptBundle(bundle);
                iterator.remove();
                getContext().addBundle(bundle);
                break;
              }
            }
          }
        }
      });
    }
  }

  private void adaptBundle(@Nonnull SelectedBundle bundle) {
    String url = bundle.getBundlePath();
    assert url != null;
    if (url.contains(ORG_ECLIPSE_EQUINOX_COMMON_URL)) {
      bundle.setStartLevel(2);
      bundle.setStartAfterInstallation(true);
    }
    else if (url.contains(ORG_ECLIPSE_UPDATE_CONFIGURATOR_URL)) {
      bundle.setStartLevel(3);
      bundle.setStartAfterInstallation(true);
    }
    else if (url.contains(ORG_ECLIPSE_CORE_RUNTIME_URL)) {
      bundle.setStartLevel(4);
      bundle.setStartAfterInstallation(true);
    }
  }

  private SelectedBundle createSelectedFrameworkBundle(String path) {
    String bundleName = CachingBundleInfoProvider.getBundleSymbolicName(path);
    SelectedBundle bundle = null;
    if (bundleName != null) {
      String bundleVersion = CachingBundleInfoProvider.getBundleVersions(path);
      bundle = new SelectedBundle(bundleName + " - " + bundleVersion, path, BundleType.FrameworkBundle);
    }
    return bundle;
  }
}
