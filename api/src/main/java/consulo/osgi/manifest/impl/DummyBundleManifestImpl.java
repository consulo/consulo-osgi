package consulo.osgi.manifest.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.osmorc.manifest.lang.psi.Header;
import org.osmorc.manifest.lang.psi.ManifestFile;
import com.intellij.psi.NavigatablePsiElement;
import consulo.osgi.manifest.BundleManifest;

/**
 * @author VISTALL
 * @since 15:13/29.04.13
 */
public class DummyBundleManifestImpl extends AbstractBundleManifestImpl
{
	public static BundleManifest INSTANCE = new DummyBundleManifestImpl();

	@Nullable
	@Override
	protected Header getHeaderByName(@Nonnull String heaaderName)
	{
		return null;
	}

	@Nullable
	@Override
	public ManifestFile getManifestFile()
	{
		return null;
	}

	@Override
	public NavigatablePsiElement getNavigateTargetByHeaderName(@Nonnull String name)
	{
		return null;
	}

	@Override
	public void setHeaderValue(@Nonnull String key, @Nonnull String value)
	{
	}

	@Override
	public long getModificationCount()
	{
		return -1;
	}
}
