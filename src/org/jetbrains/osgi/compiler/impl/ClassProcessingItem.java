package org.jetbrains.osgi.compiler.impl;

import com.intellij.openapi.compiler.FileProcessingCompiler;
import com.intellij.openapi.compiler.ValidityState;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 17:19/06.05.13
 */
public class ClassProcessingItem implements FileProcessingCompiler.ProcessingItem {
  private final VirtualFile myVirtualFile;
  private final VirtualFile myModuleOutputDirectory;

  public ClassProcessingItem(VirtualFile virtualFile, VirtualFile moduleOutputDirectory) {
    myVirtualFile = virtualFile;
    myModuleOutputDirectory = moduleOutputDirectory;
  }

  @NotNull
  @Override
  public VirtualFile getFile() {
    return myVirtualFile;
  }

  @Nullable
  @Override
  public ValidityState getValidityState() {
    return null;
  }

  @NotNull
  public VirtualFile getModuleOutputDirectory() {
    return myModuleOutputDirectory;
  }
}
