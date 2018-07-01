package consulo.osgi.compiler.artifact.bndTools;

import java.util.List;

import aQute.libg.reporter.Reporter;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompilerMessageCategory;

/**
 * @author VISTALL
 * @since 14:50/06.05.13
 */
public class BndReporter implements Reporter
{
	private final CompileContext myCompilerContext;

	public BndReporter(CompileContext compilerContext)
	{
		myCompilerContext = compilerContext;
	}

	@Override
	public void error(String s, Object... args)
	{
		myCompilerContext.addMessage(CompilerMessageCategory.ERROR, String.format(s, args), null, -1, -1);
	}

	@Override
	public void warning(String s, Object... args)
	{
		myCompilerContext.addMessage(CompilerMessageCategory.WARNING, String.format(s, args), null, -1, -1);
	}

	@Override
	public void progress(String s, Object... args)
	{
		myCompilerContext.addMessage(CompilerMessageCategory.INFORMATION, String.format(s, args), null, -1, -1);
	}

	@Override
	public void trace(String s, Object... args)
	{
	}

	@Override
	public List<String> getWarnings()
	{
		return null;
	}

	@Override
	public List<String> getErrors()
	{
		return null;
	}

	@Override
	public boolean isPedantic()
	{
		return false;
	}
}
