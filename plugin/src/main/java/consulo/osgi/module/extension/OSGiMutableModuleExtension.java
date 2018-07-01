package consulo.osgi.module.extension;

import javax.annotation.Nonnull;
import javax.swing.JComponent;

import javax.annotation.Nullable;
import consulo.module.extension.MutableModuleExtension;
import consulo.roots.ModuleRootLayer;

/**
 * @author VISTALL
 * @since 16:02/30.05.13
 */
public class OSGiMutableModuleExtension extends OSGiModuleExtension implements MutableModuleExtension<OSGiModuleExtension>
{

	public OSGiMutableModuleExtension(@Nonnull String id, @Nonnull ModuleRootLayer module)
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
	public boolean isModified(@Nonnull OSGiModuleExtension extension)
	{
		return myIsEnabled != extension.isEnabled();
	}
}
