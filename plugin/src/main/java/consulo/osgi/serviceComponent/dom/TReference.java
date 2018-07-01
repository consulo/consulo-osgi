// Generated on Mon Apr 29 12:20:26 EEST 2013
// DTD/Schema  :    http://www.osgi.org/xmlns/scr/v1.1.0

package consulo.osgi.serviceComponent.dom;

import javax.annotation.Nonnull;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;

/**
 * http://www.osgi.org/xmlns/scr/v1.1.0:Treference interface.
 *
 * @author VISTALL
 */
public interface TReference extends DomElement
{

	/**
	 * Returns the value of the name child.
	 * <pre>
	 * <h3>Attribute null:name documentation</h3>
	 * The default value of this attribute is the value of
	 * 					the interface attribute of this element. If multiple
	 * 					instances of this element within a component element
	 * 					use the same value for the interface attribute, then
	 * 					using the default value for this attribute will result
	 * 					in duplicate names. In this case, this attribute
	 * 					must be specified with a unique value.
	 * </pre>
	 *
	 * @return the value of the name child.
	 */
	@Nonnull
	GenericAttributeValue<String> getName();


	/**
	 * Returns the value of the interface child.
	 *
	 * @return the value of the interface child.
	 */
	@Nonnull
	@Required
	GenericAttributeValue<String> getInterface();


	/**
	 * Returns the value of the cardinality child.
	 *
	 * @return the value of the cardinality child.
	 */
	@Nonnull
	GenericAttributeValue<TCardinality> getCardinality();


	/**
	 * Returns the value of the policy child.
	 *
	 * @return the value of the policy child.
	 */
	@Nonnull
	GenericAttributeValue<TPolicy> getPolicy();


	/**
	 * Returns the value of the target child.
	 *
	 * @return the value of the target child.
	 */
	@Nonnull
	GenericAttributeValue<String> getTarget();


	/**
	 * Returns the value of the bind child.
	 *
	 * @return the value of the bind child.
	 */
	@Nonnull
	GenericAttributeValue<String> getBind();


	/**
	 * Returns the value of the unbind child.
	 *
	 * @return the value of the unbind child.
	 */
	@Nonnull
	GenericAttributeValue<String> getUnbind();
}
