package consulo.osgi.compiler.artifact;

import java.util.Collections;
import java.util.List;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.packaging.artifacts.ArtifactTemplate;
import com.intellij.packaging.artifacts.ArtifactType;
import com.intellij.packaging.elements.CompositePackagingElement;
import com.intellij.packaging.elements.PackagingElementOutputKind;
import com.intellij.packaging.elements.PackagingElementResolvingContext;
import com.intellij.packaging.impl.artifacts.ArtifactUtil;
import consulo.java.packaging.impl.elements.JarArchivePackagingElement;
import consulo.osgi.module.extension.OSGiModuleExtension;

/**
 * @author VISTALL
 * @since 14:47/05.05.13
 */
public class OSGiArtifactType extends ArtifactType
{
	public static ArtifactType getInstance()
	{
		return EP_NAME.findExtension(OSGiArtifactType.class);
	}

	public OSGiArtifactType()
	{
		super("osgi", "OSGi Artifact");
	}

	@NotNull
	@Override
	public Icon getIcon()
	{
		return AllIcons.Nodes.Artifact;
	}

	@Override
	public boolean isAvailableForAdd(@NotNull ModulesProvider modulesProvider)
	{
		return ModuleUtil.hasModuleExtension(modulesProvider, OSGiModuleExtension.class);
	}

	@Nullable
	@Override
	public String getDefaultPathFor(@NotNull PackagingElementOutputKind kind)
	{
		return "/";
	}

	@NotNull
	@Override
	public CompositePackagingElement<?> createRootElement(@NotNull String artifactName)
	{
		return new JarArchivePackagingElement(ArtifactUtil.suggestArtifactFileName(artifactName) + ".jar");
	}

	@NotNull
	@Override
	public List<? extends ArtifactTemplate> getNewArtifactTemplates(@NotNull PackagingElementResolvingContext context)
	{
		return Collections.singletonList(new OSGiArtifactTemplate(context));
	}
}
