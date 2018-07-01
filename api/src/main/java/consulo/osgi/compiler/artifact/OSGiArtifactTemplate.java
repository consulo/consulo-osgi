package consulo.osgi.compiler.artifact;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.roots.ui.configuration.ChooseModulesDialog;
import com.intellij.packaging.artifacts.ArtifactTemplate;
import com.intellij.packaging.elements.CompositePackagingElement;
import com.intellij.packaging.elements.PackagingElementFactory;
import com.intellij.packaging.elements.PackagingElementResolvingContext;
import consulo.java.roots.SpecialDirUtil;
import consulo.osgi.OSGiConstants;
import consulo.osgi.module.OSGiModuleExtensionUtil;
import consulo.osgi.module.extension.OSGiModuleExtension;

/**
 * @author VISTALL
 * @since 19:57/05.05.13
 */
public class OSGiArtifactTemplate extends ArtifactTemplate
{
	private PackagingElementResolvingContext myContext;

	public OSGiArtifactTemplate(PackagingElementResolvingContext context)
	{
		myContext = context;
	}

	@Nullable
	@Override
	public NewArtifactConfiguration createArtifact()
	{
		List<Module> modules = new ArrayList<Module>();
		for(Module module : ModuleManager.getInstance(myContext.getProject()).getModules())
		{
			if(OSGiModuleExtensionUtil.findExtension(module) != null)
			{
				modules.add(module);
			}
		}
		ChooseModulesDialog dialog = new ChooseModulesDialog(myContext.getProject(), modules, "Choose Module", "Choose module for artifact creation");
		dialog.setSingleSelectionMode();
		final List<Module> selectedModules = dialog.showAndGetResult();
		if(selectedModules.size() != 1)
		{
			return null;
		}
		final OSGiModuleExtension facet = OSGiModuleExtensionUtil.findExtension(selectedModules.get(0));
		return doCreateArtifactTemplate(facet);
	}

	public static NewArtifactConfiguration doCreateArtifactTemplate(OSGiModuleExtension facet)
	{
		final Module module = facet.getModule();
		final String name = module.getName();

		final PackagingElementFactory factory = PackagingElementFactory.getInstance();
		final CompositePackagingElement<?> archive = OSGiArtifactType.getInstance().createRootElement(name);

		archive.addOrFindChild(factory.createModuleOutput(module));

		final CompositePackagingElement<?> osgiRoot = archive.addOrFindChild(factory.createDirectory(OSGiConstants.OSGI_INFO_ROOT));
		osgiRoot.addOrFindChild(factory.createDirectoryCopyWithParentDirectories(facet.getOSGiInf(), "/"));

		final CompositePackagingElement<?> metaRoot = archive.addOrFindChild(factory.createDirectory(SpecialDirUtil.META_INF));
		metaRoot.addOrFindChild(factory.createDirectoryCopyWithParentDirectories(facet.getMETAInf(), "/"));

		return new NewArtifactConfiguration(archive, "OSGi:" + name, OSGiArtifactType.getInstance());
	}

	@Override
	public String getPresentableName()
	{
		return "By module";
	}
}
