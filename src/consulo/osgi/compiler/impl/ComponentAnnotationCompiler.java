package consulo.osgi.compiler.impl;

import java.io.DataInput;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import aQute.bnd.make.component.ComponentAnnotationReader;
import aQute.lib.osgi.Clazz;
import aQute.lib.osgi.FileResource;
import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.openapi.compiler.ClassInstrumentingCompiler;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.compiler.ValidityState;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.vcsUtil.VcsUtil;
import consulo.osgi.OSGiConstants;
import consulo.osgi.compiler.artifact.bndTools.BndReporter;
import consulo.osgi.compiler.artifact.bndTools.serviceComponent.BndServiceComponentUtil;
import consulo.osgi.module.OSGiModuleExtensionUtil;

/**
 * @author VISTALL
 * @since 17:10/06.05.13
 */
public class ComponentAnnotationCompiler implements ClassInstrumentingCompiler
{
	@NotNull
	@Override
	public ProcessingItem[] getProcessingItems(CompileContext context)
	{
		List<ProcessingItem> items = new ArrayList<ProcessingItem>();

		for(Module module : context.getCompileScope().getAffectedModules())
		{
			if(module == null || OSGiModuleExtensionUtil.findExtension(module) == null)
			{
				continue;
			}

			final VirtualFile moduleOutputDirectory = context.getModuleOutputDirectory(module);
			if(moduleOutputDirectory == null)
			{
				continue;
			}
			List<VirtualFile> list = new ArrayList<VirtualFile>();
			VcsUtil.collectFiles(moduleOutputDirectory, list, true, false);

			for(VirtualFile maybeClassFile : list)
			{
				if(maybeClassFile.getFileType() != JavaClassFileType.INSTANCE)
				{
					continue;
				}

				items.add(new ClassProcessingItem(maybeClassFile, moduleOutputDirectory));
			}
		}
		return items.toArray(new ProcessingItem[items.size()]);
	}

	@Override
	public ProcessingItem[] process(CompileContext context, ProcessingItem[] items)
	{
		context.getProgressIndicator().setText("Generating service component files");

		List<ProcessingItem> itemList = new ArrayList<ProcessingItem>(items.length);
		for(ProcessingItem processingItem : items)
		{
			ClassProcessingItem classProcessingItem = (ClassProcessingItem) processingItem;
			final VirtualFile file = classProcessingItem.getFile();

			final String path = file.getPath();

			Clazz clazz = new Clazz(path, new FileResource(new File(path)));
			try
			{
				final Map<String, String> definition = ComponentAnnotationReader.getDefinition(clazz, new BndReporter(context));
				if(definition == null)
				{
					continue;
				}

				final VirtualFile moduleOutputDirectory = classProcessingItem.getModuleOutputDirectory();

				final String name = BndServiceComponentUtil.getName(definition, clazz);
				final String fileText = BndServiceComponentUtil.toXml(definition, name);

				final File outFile = new File(moduleOutputDirectory.getPath() + "/" + OSGiConstants.OSGI_INFO_ROOT, name + ".xml");
				FileUtilRt.createIfNotExists(outFile);

				FileUtil.writeToFile(outFile, fileText);
				itemList.add(new ProcessingItem()
				{
					@NotNull
					@Override
					public VirtualFile getFile()
					{
						return LocalFileSystem.getInstance().refreshAndFindFileByIoFile(outFile);
					}

					@Nullable
					@Override
					public ValidityState getValidityState()
					{
						return null;
					}
				});

			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

		}
		return itemList.toArray(new ProcessingItem[itemList.size()]);
	}

	@NotNull
	@Override
	public String getDescription()
	{
		return getClass().getSimpleName();
	}

	@Override
	public boolean validateConfiguration(CompileScope scope)
	{
		return true;
	}

	@Override
	public void init(@NotNull CompilerManager compilerManager)
	{
	}

	@Override
	public ValidityState createValidityState(DataInput in) throws IOException
	{
		return null;
	}
}
