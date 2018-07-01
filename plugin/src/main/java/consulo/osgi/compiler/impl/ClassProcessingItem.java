package consulo.osgi.compiler.impl;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.openapi.compiler.FileProcessingCompiler;
import com.intellij.openapi.compiler.ValidityState;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author VISTALL
 * @since 17:19/06.05.13
 */
public class ClassProcessingItem implements FileProcessingCompiler.ProcessingItem
{
	private final VirtualFile myVirtualFile;
	private final VirtualFile myModuleOutputDirectory;

	public ClassProcessingItem(VirtualFile virtualFile, VirtualFile moduleOutputDirectory)
	{
		myVirtualFile = virtualFile;
		myModuleOutputDirectory = moduleOutputDirectory;
	}

	@Nonnull
	@Override
	public File getFile()
	{
		return VfsUtilCore.virtualToIoFile(myVirtualFile);
	}

	@Nullable
	@Override
	public ValidityState getValidityState()
	{
		return null;
	}

	@Nonnull
	public VirtualFile getModuleOutputDirectory()
	{
		return myModuleOutputDirectory;
	}
}
