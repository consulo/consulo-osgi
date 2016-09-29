package consulo.osgi.ide.codeInspection;

import com.intellij.util.xml.highlighting.BasicDomElementsInspection;
import consulo.osgi.serviceComponent.dom.TComponent;

/**
 * @author VISTALL
 * @since 14:34/28.04.13
 */
public class ServiceComponentResolveInspection extends BasicDomElementsInspection<TComponent>
{

	public ServiceComponentResolveInspection()
	{
		super(TComponent.class);
	}
}
