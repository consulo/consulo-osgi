package consulo.osgi.manifest.headerparser.serviceComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;
import com.intellij.vcsUtil.VcsFileUtil;
import com.intellij.vcsUtil.VcsUtil;
import consulo.osgi.module.OSGiModuleExtensionUtil;
import consulo.osgi.serviceComponent.dom.TComponent;

/**
 * @author VISTALL
 * @since 14:49/28.04.13
 */
public class ComponentXmlFilePsiReference extends PsiReferenceBase<PsiElement>
{

	private final String myFilePath;

	public ComponentXmlFilePsiReference(PsiElement element, String filePath)
	{
		super(element, new TextRange(element.getStartOffsetInParent(), element.getStartOffsetInParent() + element.getTextLength()), false);
		myFilePath = filePath;
	}

	@Nullable
	@Override
	public PsiElement resolve()
	{
		return getComponents().get(myFilePath);
	}

	@Nonnull
	@Override
	public Object[] getVariants()
	{
		final Map<String, XmlFile> components = getComponents();
		return components.keySet().toArray(new String[components.size()]);
	}

	@Nonnull
	private Map<String, XmlFile> getComponents()
	{
		final Module moduleForPsiElement = ModuleUtil.findModuleForPsiElement(myElement);
		if(moduleForPsiElement == null)
		{
			return Collections.emptyMap();
		}
		final VirtualFile osgiInfRoot = OSGiModuleExtensionUtil.getOSGiInf(moduleForPsiElement);
		if(osgiInfRoot == null)
		{
			return Collections.emptyMap();
		}

		List<VirtualFile> files = new ArrayList<VirtualFile>();
		VcsUtil.collectFiles(osgiInfRoot, files, true, false);

		final DomManager domManager = DomManager.getDomManager(getElement().getProject());
		final PsiManager manager = PsiManager.getInstance(getElement().getProject());

		Map<String, XmlFile> map = new HashMap<String, XmlFile>();
		for(VirtualFile file : files)
		{
			PsiFile psiFile = manager.findFile(file);
			if(psiFile instanceof XmlFile)
			{
				final DomFileElement<TComponent> fileElement = domManager.getFileElement((XmlFile) psiFile, TComponent.class);
				if(fileElement != null)
				{
					map.put(VcsFileUtil.getRelativeFilePath(file, osgiInfRoot.getParent()), (XmlFile) psiFile);
				}
			}
		}

		return map;
	}
}
