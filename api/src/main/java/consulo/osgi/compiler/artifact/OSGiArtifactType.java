package consulo.osgi.compiler.artifact;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import javax.annotation.Nullable;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.packaging.artifacts.ArtifactTemplate;
import com.intellij.packaging.artifacts.ArtifactType;
import com.intellij.packaging.elements.CompositePackagingElement;
import com.intellij.packaging.elements.PackagingElementFactory;
import com.intellij.packaging.elements.PackagingElementOutputKind;
import com.intellij.packaging.elements.PackagingElementResolvingContext;
import com.intellij.packaging.impl.artifacts.ArtifactUtil;
import consulo.java.packaging.impl.elements.JarArchivePackagingElement;
import consulo.osgi.module.extension.OSGiModuleExtension;
import consulo.ui.image.Image;

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

	@Nonnull
	@Override
	public Image getIcon()
	{
		return AllIcons.Nodes.Artifact;
	}

	@Override
	public boolean isAvailableForAdd(@Nonnull ModulesProvider modulesProvider)
	{
		return ModuleUtil.hasModuleExtension(modulesProvider, OSGiModuleExtension.class);
	}

	@Nullable
	@Override
	public String getDefaultPathFor(@Nonnull PackagingElementOutputKind kind)
	{
		return "/";
	}

	@Nonnull
	@Override
	public CompositePackagingElement<?> createRootElement(@Nonnull PackagingElementFactory factory, @Nonnull String artifactName)
	{
		return new JarArchivePackagingElement(ArtifactUtil.suggestArtifactFileName(artifactName) + ".jar");
	}

	@Nonnull
	@Override
	public List<? extends ArtifactTemplate> getNewArtifactTemplates(@Nonnull PackagingElementResolvingContext context)
	{
		return Collections.singletonList(new OSGiArtifactTemplate(context));
	}
}
