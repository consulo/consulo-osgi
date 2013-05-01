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
package org.jetbrains.osgi.manifest.impl;

import com.intellij.openapi.vfs.ReadonlyStatusHandler;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osmorc.manifest.lang.ManifestFileType;
import org.osmorc.manifest.lang.ManifestTokenType;
import org.osmorc.manifest.lang.psi.Header;
import org.osmorc.manifest.lang.psi.ManifestFile;
import org.osmorc.manifest.lang.psi.Section;

/**
 * @author Robert F. Beeger (robert@beeger.net)
 * @author Jan Thom&auml; (janthomae@janthomae.de)
 */
public class BundleManifestImpl extends AbstractBundleManifestImpl {
  private static final String SET_HEADER = "%s : %s\n";

  @NotNull
  private final ManifestFile myManifestFile;

  public BundleManifestImpl(@NotNull ManifestFile manifestFile) {
    myManifestFile = manifestFile;
  }

  @Nullable
  @Override
  protected Header getHeaderByName(@NotNull String heaaderName) {
    return myManifestFile.getHeaderByName(heaaderName);
  }

  @NotNull
  @Override
  public ManifestFile getManifestFile() {
    return myManifestFile;
  }

  @Override
  public NavigatablePsiElement getNavigateTargetByHeaderName(@NotNull String name) {
    return (NavigatablePsiElement)getHeaderByName(name);
  }

  @Override
  public void setHeaderValue(@NotNull String key, @NotNull String value) {
    ReadonlyStatusHandler.OperationStatus status =
      ReadonlyStatusHandler.getInstance(myManifestFile.getProject()).ensureFilesWritable(myManifestFile.getVirtualFile());
    if(status.hasReadonlyFiles()) {
      return;
    }

    PsiFile fromText = PsiFileFactory.getInstance(myManifestFile.getProject())
      .createFileFromText("DUMMY.MF", ManifestFileType.INSTANCE, String.format(SET_HEADER, key, value));

    Header newHeader = PsiTreeUtil.getChildOfType(fromText.getFirstChild(), Header.class);

    assert newHeader != null;

    Header headerByName = getHeaderByName(key);
    if (headerByName == null) {
      Section section = (Section)myManifestFile.getFirstChild();

      String sectionText = section.getText();
      if (sectionText.charAt(sectionText.length() - 1) != '\n') {
        PsiElement lastChild = section.getLastChild();
        if (lastChild instanceof Header) {
          Header header = (Header)lastChild;
          header.getNode().addLeaf(ManifestTokenType.NEWLINE, "\n", null);
        }
      }

      section.add(newHeader);
    }
    else {
      headerByName.replace(newHeader);
    }
  }

  @Override
  public long getModificationCount() {
    return myManifestFile.getModificationStamp();
  }
}
