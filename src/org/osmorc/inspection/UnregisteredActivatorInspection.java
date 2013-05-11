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
package org.osmorc.inspection;

import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.intellij.codeInspection.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.osgi.facet.OSGiFacet;
import org.jetbrains.osgi.facet.OSGiFacetUtil;
import org.jetbrains.osgi.manifest.BundleManifest;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.Constants;

/**
 * Inspection that reports classes implementing BundleActivator which are not registered in the manifest / facet
 * config.
 *
 * @author <a href="mailto:janthomae@janthomae.de">Jan Thom&auml;</a>
 * @author Robert F. Beeger (robert@beeger.net)
 * @version $Id$
 */
public class UnregisteredActivatorInspection extends LocalInspectionTool {

  @Nls
  @NotNull
  public String getGroupDisplayName() {
    return "OSGi";
  }

  public boolean isEnabledByDefault() {
    return true;
  }

  @NotNull
  public HighlightDisplayLevel getDefaultLevel() {
    return HighlightDisplayLevel.ERROR;
  }

  @Nls
  @NotNull
  public String getDisplayName() {
    return "Bundle Activator not registered";
  }

  @NonNls
  @NotNull
  public String getShortName() {
    return "osmorcUnregisteredActivator";
  }

  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    return new JavaElementVisitor() {
      @Override
      public void visitClass(PsiClass psiClass) {
        OSGiFacet facet = OSGiFacetUtil.findFacet(psiClass);
        if (facet != null) {
          BundleManifest bundleManifest = facet.getManifest();
          for (PsiType type : psiClass.getSuperTypes()) {
            if (type.equalsToText(BundleActivator.class.getName())) {
              String activatorName = psiClass.getQualifiedName();
              if (activatorName == null) {
                continue;
              }

              if (!activatorName.equals(bundleManifest.getBundleActivator())) {
                holder.registerProblem(psiClass.getNameIdentifier(), "Bundle activator is not set up in facet configuration.",
                                       ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                                       new RegisterActivatorInManifestQuickfix(activatorName, bundleManifest));
              }
            }
          }
        }
      }
    };
  }

  private class RegisterActivatorInManifestQuickfix implements LocalQuickFix {
    private static final String NAME = "Register Activator In Manifest";
    private static final String FAMILY = "OSGi";
    private final String activatorClassName;
    private final BundleManifest myBundleManifest;

    private RegisterActivatorInManifestQuickfix(@NotNull final String activatorClassName, @NotNull final BundleManifest bundleManifest) {
      this.activatorClassName = activatorClassName;
      this.myBundleManifest = bundleManifest;
    }

    @NotNull
    public String getName() {
      return NAME;
    }

    @NotNull
    public String getFamilyName() {
      return FAMILY;
    }

    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
      myBundleManifest.setHeaderValue(Constants.BUNDLE_ACTIVATOR, activatorClassName);
    }
  }
}
