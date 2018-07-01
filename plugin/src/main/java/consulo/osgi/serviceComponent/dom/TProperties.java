// Generated on Mon Apr 29 12:20:26 EEST 2013
// DTD/Schema  :    http://www.osgi.org/xmlns/scr/v1.1.0

package consulo.osgi.serviceComponent.dom;

import javax.annotation.Nonnull;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;

/**
 * http://www.osgi.org/xmlns/scr/v1.1.0:Tproperties interface.
 *
 * @author VISTALL
 */
public interface TProperties extends DomElement
{

	/**
	 * Returns the value of the entry child.
	 *
	 * @return the value of the entry child.
	 */
	@Nonnull
	@Required
	GenericAttributeValue<String> getEntry();


}
