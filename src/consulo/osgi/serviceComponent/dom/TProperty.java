// Generated on Mon Apr 29 12:20:26 EEST 2013
// DTD/Schema  :    http://www.osgi.org/xmlns/scr/v1.1.0

package consulo.osgi.serviceComponent.dom;

import org.jetbrains.annotations.NotNull;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;

/**
 * http://www.osgi.org/xmlns/scr/v1.1.0:Tproperty interface.
 *
 * @author VISTALL
 */
public interface TProperty extends DomElement
{

	/**
	 * Returns the value of the simple content.
	 *
	 * @return the value of the simple content.
	 */
	@NotNull
	@Required
	String getValue1();

	/**
	 * Sets the value of the simple content.
	 *
	 * @param value1 the new value to set
	 */
	void setValue1(@NotNull String value1);


	/**
	 * Returns the value of the name child.
	 *
	 * @return the value of the name child.
	 */
	@NotNull
	@Required
	GenericAttributeValue<String> getName();


	/**
	 * Returns the value of the value child.
	 *
	 * @return the value of the value child.
	 */
	@NotNull
	@Attribute("value")
	GenericAttributeValue<String> getValue2();


	/**
	 * Returns the value of the type child.
	 *
	 * @return the value of the type child.
	 */
	@NotNull
	GenericAttributeValue<TJavaTypes> getType();
}
