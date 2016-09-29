package consulo.osgi.module.extension;

import javax.swing.JComponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import consulo.module.extension.MutableModuleExtension;
import consulo.roots.ModuleRootLayer;

/**
 * @author VISTALL
 * @since 16:02/30.05.13
 */
public class OSGiMutableModuleExtension extends OSGiModuleExtension implements MutableModuleExtension<OSGiModuleExtension>
{

	public OSGiMutableModuleExtension(@NotNull String id, @NotNull ModuleRootLayer module)
	{
		super(id, module);
	}

	@Nullable
	@Override
	public JComponent createConfigurablePanel(@Nullable Runnable runnable)
	{
		return null;
	}

	@Override
	public void setEnabled(boolean b)
	{
		myIsEnabled = b;
	}

	@Override
	public boolean isModified(@NotNull OSGiModuleExtension extension)
	{
		return myIsEnabled != extension.isEnabled();
	}
}
