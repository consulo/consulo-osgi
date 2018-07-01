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

package org.osmorc.run.ui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Class representing a bundle that has been selected for running. This can either be some pre-jarred bundle from the
 * classpath or a module from this project.
 *
 * @author <a href="mailto:janthomae@janthomae.de">Jan Thom&auml;</a>
 * @version $Id:$
 */
public class SelectedBundle {

  private String myDisplayName;
  @Nullable
  private String myBundlePath;
  private int myStartLevel;
  private boolean myStartAfterInstallation;
  private final BundleType myBundleType;

  public SelectedBundle(@Nonnull String displayName, @Nullable String path, @Nonnull BundleType bundleType) {
    this.myDisplayName = displayName;
    myBundlePath = path;
    this.myBundleType = bundleType;
    myStartAfterInstallation = bundleType.isDefaultStartAfterInstallation();
    myStartLevel = 1;
  }

  @Nonnull
  public String getName() {
    return myDisplayName;
  }

  public void setName(@Nonnull String displayName) {
    this.myDisplayName = displayName;
  }

  @Nullable
  public String getBundlePath() {
    return myBundlePath;
  }


  public String toString() {
    return myDisplayName + (myBundlePath != null ? (" (" + myBundlePath.substring(myBundlePath.lastIndexOf("/") + 1) + ")") : "");
  }

  /**
   * Two selected bundles equal, when they point to the same URL. When the bundles are modules, they do not necessarily
   * have to point to the same URL, as the URL might be null on modules, so in this case the display name decides.
   *
   * @param o the object to check for equality
   * @return true if the given object represents the same bundle as this bundle.
   */
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (o instanceof SelectedBundle) {
      SelectedBundle other = (SelectedBundle)o;

      return myBundlePath != null ? myBundlePath.equals(other.myBundlePath) : myDisplayName.equals(myDisplayName);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return myBundlePath != null ? myBundlePath.hashCode() : myDisplayName.hashCode() ;
  }

  /**
   * @return the start level of this bundle. Unless set to something else, this is 1.
   */
  public int getStartLevel() {
    return myStartLevel;
  }

  /**
   * Returns true, if the start level of this bundle should be the default start level of the run configuration.
   *
   * @return true, if the start level of this bundle should be the default start level of the run configuration, false otherwise.
   */
  public boolean isDefaultStartLevel() {
    return myStartLevel == 0;
  }

  public void setStartLevel(int startLevel) {
    this.myStartLevel = startLevel;
  }

  public void setBundlePath(@Nullable String bundlePath) {
    this.myBundlePath = bundlePath;
  }

  public BundleType getBundleType() {
    return myBundleType;
  }

  public boolean isStartAfterInstallation() {
    return myStartAfterInstallation;
  }

  public void setStartAfterInstallation(boolean startAfterInstallation) {
    this.myStartAfterInstallation = startAfterInstallation;
  }
}
