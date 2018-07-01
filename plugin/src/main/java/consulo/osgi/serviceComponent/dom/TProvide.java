// Generated on Mon Apr 29 12:20:26 EEST 2013
// DTD/Schema  :    http://www.osgi.org/xmlns/scr/v1.1.0

package consulo.osgi.serviceComponent.dom;

import javax.annotation.Nonnull;
import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;
import com.intellij.util.xml.converters.ClassValueConverterImpl;

/**
 * http://www.osgi.org/xmlns/scr/v1.1.0:Tprovide interface.
 *
 * @author VISTALL
 */
public interface TProvide extends DomElement
{

	/**
	 * Returns the value of the interface child.
	 *
	 * @return the value of the interface child.
	 */
	@Nonnull
	@Convert(value = ClassValueConverterImpl.class, soft = false)
	@Required
	GenericAttributeValue<PsiClass> getInterface();
}
