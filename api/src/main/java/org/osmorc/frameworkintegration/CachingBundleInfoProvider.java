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

package org.osmorc.frameworkintegration;

import com.intellij.openapi.util.io.FileUtil;
import javax.annotation.Nullable;
import org.osgi.framework.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.WeakHashMap;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * This is a helper class which helps providing information about bundles that do not necessarily belong to the
 * project.
 * <p/>
 * XXX: I am aware that we have BundleManifestImpl already for this, but this one depends on PsiFiles, which do not work
 * for stuff outside the project.
 *
 * @author <a href="mailto:janthomae@janthomae.de">Jan Thom&auml;</a>
 * @version $Id:$
 */
public class CachingBundleInfoProvider {
  private static final WeakHashMap<String, Manifest> _cache = new WeakHashMap<String, Manifest>();

  private CachingBundleInfoProvider() {
  }

  /**
   * Returns true if the file at the given path is a bundle, false otherwise.
   *
   * @param path the path of the bundle
   * @return true if the file at the path is a bundle, false otherwise.
   */
  public static boolean isBundle(String path) {
    return getBundleSymbolicName(path) != null;
  }

  /**
   * Returns true if the object at the given URI can be bundlified. This is only true for jar files which are not already bundles.
   *
   * @param path the path to the object to be bundlified.
   * @return true if the object can be bundlified, false otherwise.
   */
  public static boolean canBeBundlified(String path) {
    if (isBundle(path)) return false;
    File sourceFile = new File(path);
    if (sourceFile.isDirectory()) {
      // it's an exploded directory, we cannot bundle these.
      return false;
    }
    if (!sourceFile.getName().endsWith(".jar")) {
      // it's no jar, so we won't bundlify it either.
      return false;
    }
    return true;
  }

  /**
   * Returns the symbolic name of the bundle at the given path. If the file at the given path is no bundle, returns null.
   *
   * @param path the path of the bundle
   * @return the symbolic name of the bundle or null if the file is no bundle.
   */
  @Nullable
  public static String getBundleSymbolicName(String path) {
    String symbolicName = getBundleAttribute(path, Constants.BUNDLE_SYMBOLICNAME);
    return symbolicName != null ? symbolicName.split(";", 2)[0] : null; // Only take the name and leave the parameters
  }

  /**
   * Returns the version of the bundle at the given path.
   *
   * @param path the path path the bundle to search
   * @return the version of the bundle or null if the file is no bundle
   */
  @Nullable
  public static String getBundleVersions(String path) {
    return getBundleAttribute(path, Constants.BUNDLE_VERSION);
  }


  /**
   * Returns boolean status if the given bundle is a fragment bundle.
   *
   * @param bundleUrl the path of the bundle
   * @return true if the given bundle is a fragment bundle, false if it is not or if the state could not be determined.
   */
  public static boolean isFragmentBundle(String bundleUrl) {
    String fragmentHost = getBundleAttribute(bundleUrl, Constants.FRAGMENT_HOST);
    return fragmentHost != null;
  }

  /**
   * Returns the attribute of the bundle located at the given path. If the bundle cannot be found there or the jar at
   * that location isn't a bundle, this returns null.
   *
   * @param path the path of the bundle
   * @param attribute the attribute to resolve
   * @return the attribute's value or null if there is no such bundle or no such attribute
   */
  @Nullable
  private synchronized static String getBundleAttribute(String path, String attribute) {
    final String indepPath = FileUtil.toSystemIndependentName(path);

    Manifest manifest = _cache.get(indepPath);
    if (manifest == null) {
      try {
        File bundleFile = new File(indepPath);
        if (bundleFile.isDirectory()) {
          File manifestFile = new File(bundleFile, "META-INF/MANIFEST.MF");
          if (manifestFile.exists() && !manifestFile.isDirectory()) {
            FileInputStream fileInputStream = new FileInputStream(manifestFile);

            manifest = new Manifest(fileInputStream);

            fileInputStream.close();
            _cache.put(indepPath, manifest);
          }
        }
        else {
          JarFile file = new JarFile(bundleFile);
          _cache.put(indepPath, manifest = file.getManifest());
          file.close();
        }
      }
      catch (IOException e) {
        return null;
      }
    }


    return manifest != null ? manifest.getMainAttributes().getValue(attribute) : null;
  }


  public static boolean isExploded(String path) {
    File bundleFile = new File(path);
    return bundleFile.isDirectory();
  }
}
