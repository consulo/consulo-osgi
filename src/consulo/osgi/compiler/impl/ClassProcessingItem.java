package consulo.osgi.compiler.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.compiler.FileProcessingCompiler;
import com.intellij.openapi.compiler.ValidityState;
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

	@NotNull
	@Override
	public VirtualFile getFile()
	{
		return myVirtualFile;
	}

	@Nullable
	@Override
	public ValidityState getValidityState()
	{
		return null;
	}

	@NotNull
	public VirtualFile getModuleOutputDirectory()
	{
		return myModuleOutputDirectory;
	}
}
