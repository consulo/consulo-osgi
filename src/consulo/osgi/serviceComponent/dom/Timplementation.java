// Generated on Mon Apr 29 12:20:26 EEST 2013
// DTD/Schema  :    http://www.osgi.org/xmlns/scr/v1.1.0

package consulo.osgi.serviceComponent.dom;

import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;
import com.intellij.util.xml.converters.ClassValueConverterImpl;

/**
 * http://www.osgi.org/xmlns/scr/v1.1.0:Timplementation interface.
 *
 * @author VISTALL
 */
public interface Timplementation extends DomElement
{

	/**
	 * Returns the value of the class child.
	 *
	 * @return the value of the class child.
	 */
	@NotNull
	@Attribute("class")
	@Convert(value = ClassValueConverterImpl.class, soft = false)
	@Required
	GenericAttributeValue<PsiClass> getClazz();
}
