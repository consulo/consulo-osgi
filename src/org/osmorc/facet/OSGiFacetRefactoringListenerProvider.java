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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.listeners.RefactoringElementAdapter;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.intellij.refactoring.listeners.RefactoringElementListenerProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osgi.facet.OSGiFacet;
import org.jetbrains.osgi.facet.OSGiFacetUtil;
import org.osgi.framework.Constants;

/**
 * @author Robert F. Beeger (robert@beeger.net)
 */
public class OSGiFacetRefactoringListenerProvider implements RefactoringElementListenerProvider {
  public OSGiFacetRefactoringListenerProvider() {
  }

  @Nullable
  public RefactoringElementListener getListener(final PsiElement element) {
    if (element instanceof PsiClass && OSGiFacetUtil.isBundleActivator((PsiClass) element)) {
        return new ActivatorClassRefactoringListener();
    }

    return null;
  }

  private static final class ActivatorClassRefactoringListener extends RefactoringElementAdapter {
    private ActivatorClassRefactoringListener() {
    }

    public void elementRenamedOrMoved(@NotNull final PsiElement newElement) {
      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        public void run() {
          final OSGiFacet facet = OSGiFacetUtil.findFacet(newElement);
          if(facet == null) {
            return;
          }
          facet.getManifest().setHeaderValue(Constants.BUNDLE_ACTIVATOR, ((PsiClass)newElement).getQualifiedName());
        }
      });
    }

    @Override
    public void undoElementMovedOrRenamed(@NotNull final PsiElement newElement, @NotNull final String oldQualifiedName) {
      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        @Override
        public void run() {
          final OSGiFacet facet = OSGiFacetUtil.findFacet(newElement);
          if(facet == null) {
            return;
          }
          facet.getManifest().setHeaderValue(Constants.BUNDLE_ACTIVATOR, oldQualifiedName);
        }
      });
    }
  }
}
