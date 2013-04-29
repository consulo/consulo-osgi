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
package org.osmorc.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.facet.FacetTypeRegistry;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleServiceManager;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osmorc.settings.ProjectSettings;

/**
 * The Osmorc facet.
 * <p/>
 *
 * @author Robert F. Beeger (robert@beeger.net)
 */
public class OsmorcFacet extends Facet<OsmorcFacetConfiguration> {
  public OsmorcFacet(@NotNull Module module) {
    this(FacetTypeRegistry.getInstance().findFacetType(OsmorcFacetType.ID), module, new OsmorcFacetConfiguration(), null, "OSGi");
  }

  public OsmorcFacet(@NotNull FacetType facetType,
                     @NotNull Module module,
                     @NotNull OsmorcFacetConfiguration configuration,
                     @Nullable Facet underlyingFacet,
                     final String name) {
    super(facetType, module, name, configuration, underlyingFacet);
    configuration.setFacet(this);
  }

  @NotNull
  public String getManifestLocation() {
    if (getConfiguration().isUseProjectDefaultManifestFileLocation()) {

      final ProjectSettings projectSettings = ModuleServiceManager.getService(getModule(), ProjectSettings.class);
      return projectSettings.getDefaultManifestFileLocation();
    }
    else {
      return getConfiguration().getManifestLocation();
    }
  }

  /**
   * Checks if the given file is the manifest for this facet.
   *
   * @param file the file to check
   * @return true if the given file is the manifest for this facet, false otherwise.
   */
  public boolean isManifestForThisFacet(@NotNull VirtualFile file) {
    VirtualFile manifestFile = getManifestFile();
    return manifestFile != null && file.getPath().equals(manifestFile.getPath());
  }

  /**
   * Returns the manifest file for this facet.
   *
   * @return the manifest file. If the manifest is automatically generated, returns null.
   */
  @Nullable
  public VirtualFile getManifestFile() {
    if (getConfiguration().isOsmorcControlsManifest()) {
      return null;
    }
    String path = getManifestLocation();
    path = path.replace('\\', '/');

    VirtualFile[] contentRoots = ModuleRootManager.getInstance(getModule()).getContentRoots();
    for (VirtualFile contentRoot : contentRoots) {
      VirtualFile manifestFile = contentRoot.findFileByRelativePath(path);
      if (manifestFile != null) {
        return manifestFile;
      }
    }

    return null;
  }
}
