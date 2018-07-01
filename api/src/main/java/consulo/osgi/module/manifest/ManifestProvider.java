package consulo.osgi.module.manifest;

import org.jdom.Element;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.intellij.openapi.components.PathMacroManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import consulo.osgi.manifest.BundleManifest;
import consulo.osgi.manifest.impl.DummyBundleManifestImpl;
import consulo.osgi.module.extension.OSGiModuleExtension;

/**
 * @author VISTALL
 * @since 14:01/29.04.13
 */
public abstract class ManifestProvider implements JDOMExternalizable
{

	public void validateAndCreate()
	{

	}

	@Override
	public void readExternal(Element element) throws InvalidDataException
	{
	}

	@Override
	public void writeExternal(Element element) throws WriteExternalException
	{
	}

	@Nonnull
	public final BundleManifest getBundleManifest(@Nonnull OSGiModuleExtension facet)
	{
		BundleManifest bundleManifestImpl = getBundleManifestImpl(facet);
		return bundleManifestImpl == null ? DummyBundleManifestImpl.INSTANCE : bundleManifestImpl;
	}

	@Nonnull
	public abstract ManifestProviderConfigurable createConfigurable(Module module);

	@Nullable
	protected abstract BundleManifest getBundleManifestImpl(OSGiModuleExtension facet);

	public void expandPaths(PathMacroManager pathMacroManager)
	{
	}
}
