package consulo.osgi.module.manifest;

import javax.annotation.Nonnull;
import com.intellij.openapi.options.UnnamedConfigurable;

/**
 * @author VISTALL
 * @since 14:19/29.04.13
 */
public abstract class ManifestProviderConfigurable<T> implements UnnamedConfigurable
{
	protected final T myManifestProvider;

	protected ManifestProviderConfigurable(T manifestProvider)
	{
		myManifestProvider = manifestProvider;
	}

	public void setEnabled(boolean val)
	{

	}

	@Nonnull
	public abstract String getHeaderName();
}
