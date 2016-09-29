// Generated on Mon Apr 29 12:20:26 EEST 2013
// DTD/Schema  :    http://www.osgi.org/xmlns/scr/v1.1.0

package consulo.osgi.serviceComponent.dom;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.util.xml.DefinesXml;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;

/**
 * http://www.osgi.org/xmlns/scr/v1.1.0:Tcomponent interface.
 *
 * @author VISTALL
 */
@DefinesXml
public interface TComponent extends DomElement
{

	/**
	 * Returns the value of the enabled child.
	 *
	 * @return the value of the enabled child.
	 */
	@NotNull
	GenericAttributeValue<Boolean> getEnabled();


	/**
	 * Returns the value of the name child.
	 * <pre>
	 * <h3>Attribute null:name documentation</h3>
	 * The default value of this attribute is the value of
	 * 					the class attribute of the nested implementation
	 * 					element. If multiple component elements use the same
	 * 					value for the class attribute of their nested
	 * 					implementation element, then using the default value
	 * 					for this attribute will result in duplicate names.
	 * 					In this case, this attribute must be specified with
	 * 					a unique value.
	 * </pre>
	 *
	 * @return the value of the name child.
	 */
	@NotNull
	GenericAttributeValue<String> getName();


	/**
	 * Returns the value of the factory child.
	 *
	 * @return the value of the factory child.
	 */
	@NotNull
	GenericAttributeValue<String> getFactory();


	/**
	 * Returns the value of the immediate child.
	 *
	 * @return the value of the immediate child.
	 */
	@NotNull
	GenericAttributeValue<Boolean> getImmediate();


	/**
	 * Returns the value of the configuration-policy child.
	 *
	 * @return the value of the configuration-policy child.
	 */
	@NotNull
	GenericAttributeValue<TConfigurationPolicy> getConfigurationPolicy();


	/**
	 * Returns the value of the activate child.
	 *
	 * @return the value of the activate child.
	 */
	@NotNull
	GenericAttributeValue<String> getActivate();


	/**
	 * Returns the value of the deactivate child.
	 *
	 * @return the value of the deactivate child.
	 */
	@NotNull
	GenericAttributeValue<String> getDeactivate();


	/**
	 * Returns the value of the modified child.
	 *
	 * @return the value of the modified child.
	 */
	@NotNull
	GenericAttributeValue<String> getModified();


	/**
	 * Returns the value of the service child.
	 *
	 * @return the value of the service child.
	 */
	@Nullable
	TService getService();


	/**
	 * Returns the list of reference children.
	 *
	 * @return the list of reference children.
	 */
	@NotNull
	List<TReference> getReferences();

	/**
	 * Adds new child to the list of reference children.
	 *
	 * @return created child
	 */
	TReference addReference();


	/**
	 * Returns the value of the implementation child.
	 *
	 * @return the value of the implementation child.
	 */
	@NotNull
	@Required
	Timplementation getImplementation();


	/**
	 * Returns the list of property children.
	 *
	 * @return the list of property children.
	 */
	@NotNull
	List<TProperty> getProperties();

	/**
	 * Adds new child to the list of property children.
	 *
	 * @return created child
	 */
	TProperty addProperty();


	/**
	 * Returns the list of properties children.
	 *
	 * @return the list of properties children.
	 */
	@NotNull
	List<TProperties> getPropertieses();

	/**
	 * Adds new child to the list of properties children.
	 *
	 * @return created child
	 */
	TProperties addProperties();
}
