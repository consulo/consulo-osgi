package consulo.osgi.module.manifest.impl.ui;

import javax.swing.JComponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osmorc.manifest.lang.psi.ManifestFile;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.vcsUtil.VcsUtil;
import consulo.osgi.OSGiConstants;
import consulo.osgi.manifest.BundleManifest;
import consulo.osgi.manifest.impl.BundleManifestImpl;
import consulo.osgi.module.extension.OSGiModuleExtension;
import consulo.osgi.module.manifest.ManifestProvider;
import consulo.osgi.module.manifest.ManifestProviderConfigurable;

/**
 * @author VISTALL
 * @since 14:06/29.04.13
 */
public class UseExistingManifestManifestProvider extends ManifestProvider
{
	@NotNull
	@Override
	public ManifestProviderConfigurable createConfigurable(Module module)
	{
		return new ManifestProviderConfigurable<UseExistingManifestManifestProvider>(this)
		{
			@NotNull
			@Override
			public String getHeaderName()
			{
				return "Use existing manifest";
			}

			@Nullable
			@Override
			public JComponent createComponent()
			{
				return null;
			}

			@Override
			public boolean isModified()
			{
				return false;
			}

			@Override
			public void apply() throws ConfigurationException
			{
			}

			@Override
			public void reset()
			{
			}

			@Override
			public void disposeUIResources()
			{
			}
		};
	}

	@Nullable
	@Override
	protected BundleManifest getBundleManifestImpl(OSGiModuleExtension facet)
	{
		final VirtualFile virtualFile = VcsUtil.getVirtualFile(facet.getMETAInf() + "/" + OSGiConstants.MANIFEST_NAME);
		if(virtualFile == null)
		{
			return null;
		}
		PsiManager manager = PsiManager.getInstance(facet.getModule().getProject());

		PsiFile file = manager.findFile(virtualFile);
		if(!(file instanceof ManifestFile))
		{
			return null;
		}
		return new BundleManifestImpl((ManifestFile) file);
	}
}
