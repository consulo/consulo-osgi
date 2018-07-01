package consulo.osgi.serviceComponent;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileDescription;
import consulo.osgi.OSGiIcons;
import consulo.osgi.module.OSGiModuleExtensionUtil;
import consulo.osgi.serviceComponent.dom.TComponent;
import consulo.ui.image.Image;

/**
 * @author VISTALL
 * @since 14:16/28.04.13
 */
public class ServiceComponentDomFileDescription extends DomFileDescription<TComponent>
{
	public ServiceComponentDomFileDescription()
	{
		super(TComponent.class, "component");
		registerNamespacePolicy("scr", "http://www.osgi.org/xmlns/scr/v1.1.0");
	}

	@Override
	public boolean isMyFile(@NotNull XmlFile file)
	{
		final VirtualFile virtualFile = file.getVirtualFile();
		if(virtualFile == null)
		{
			return false;
		}

		Module module = ModuleUtil.findModuleForFile(virtualFile, file.getProject());

		if(module == null)
		{
			return false;
		}

		VirtualFile osGiInf = OSGiModuleExtensionUtil.getOSGiInf(module);
		return osGiInf != null && VfsUtilCore.isAncestor(osGiInf, virtualFile, false);
	}

	@Nullable
	@Override
	public Image getFileIcon(@Iconable.IconFlags int flags)
	{
		return OSGiIcons.OsgiComponentFile;
	}
}
