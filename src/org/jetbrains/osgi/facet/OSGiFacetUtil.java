package org.jetbrains.osgi.facet;

import com.intellij.facet.FacetManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.vcsUtil.VcsUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 17:57/29.04.13
 */
public class OSGiFacetUtil {
  public static OSGiFacet findFacet(@NotNull Module module) {
    return FacetManager.getInstance(module).getFacetByType(OSGiFacetType.ID);
  }

  @Nullable
  public static VirtualFile getOSGiInf(@NotNull Module module) {
    OSGiFacet facet = findFacet(module);
    if(facet == null) {
      return null;
    }

    return VcsUtil.getVirtualFile(facet.getConfiguration().getOsgiInfLocation());
  }
}
