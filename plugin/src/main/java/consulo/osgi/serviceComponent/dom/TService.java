// Generated on Mon Apr 29 12:20:26 EEST 2013
// DTD/Schema  :    http://www.osgi.org/xmlns/scr/v1.1.0

package consulo.osgi.serviceComponent.dom;

import java.util.List;

import javax.annotation.Nonnull;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;

/**
 * http://www.osgi.org/xmlns/scr/v1.1.0:Tservice interface.
 *
 * @author VISTALL
 */
public interface TService extends DomElement
{

	/**
	 * Returns the value of the servicefactory child.
	 *
	 * @return the value of the servicefactory child.
	 */
	@Nonnull
	GenericAttributeValue<Boolean> getServicefactory();


	/**
	 * Returns the list of provide children.
	 *
	 * @return the list of provide children.
	 */
	@Nonnull
	@Required
	List<TProvide> getProvides();

	/**
	 * Adds new child to the list of provide children.
	 *
	 * @return created child
	 */
	TProvide addProvide();
}
