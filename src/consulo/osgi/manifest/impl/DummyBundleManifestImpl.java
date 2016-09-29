package consulo.osgi.manifest.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
	protected Header getHeaderByName(@NotNull String heaaderName)
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
	public NavigatablePsiElement getNavigateTargetByHeaderName(@NotNull String name)
	{
		return null;
	}

	@Override
	public void setHeaderValue(@NotNull String key, @NotNull String value)
	{
	}

	@Override
	public long getModificationCount()
	{
		return -1;
	}
}
