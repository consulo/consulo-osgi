package org.osmorc.facet;

import com.intellij.facet.FacetManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 13:55/28.04.13
 */
public class OsmorcFacetUtil {
  /**
   * Returns the Osmorc facet for the given module.
   *
   * @param module the module
   * @return the Osmorc facet of this module or null if the module doesn't have an Osmorc facet.
   */
  @Nullable
  public static OsmorcFacet getInstance(@NotNull Module module) {
    return FacetManager.getInstance(module).getFacetByType(OsmorcFacetType.ID);
  }

  /**
   * Determines the module to which the given element belongs and returns the Osmorc facet for this module.
   *
   * @param element the element
   * @return the Osmorc facet of the module to which the element belongs or null if this module doesn't have an Osmorc
   *         facet or if the belonging module could not be determined.
   */
  @Nullable
  public static OsmorcFacet getInstance(@NotNull PsiElement element) {
    Module module = ModuleUtil.findModuleForPsiElement(element);
    if (module != null) {
      return getInstance(module);
    }
    return null;
  }

  /**
   * @param module the module to check
   * @return true if there is an Osmorc facet for the given module, false otherwise.
   */
  public static boolean hasOsmorcFacet(@NotNull Module module) {
    return getInstance(module) != null;
  }

  /**
   * @param element the element to check
   * @return true if the module of the element could be determined and this module has an Osmorc facet, false
   *         otherwise.
   */
  public static boolean hasOsmorcFacet(@NotNull PsiElement element) {
    Module module = ModuleUtil.findModuleForPsiElement(element);
    return module != null && hasOsmorcFacet(module);
  }
}
