// Generated on Mon Apr 29 12:20:26 EEST 2013
// DTD/Schema  :    http://www.osgi.org/xmlns/scr/v1.1.0

package org.jetbrains.osmorc2.serviceComponent.dom;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * http://www.osgi.org/xmlns/scr/v1.1.0:Tservice interface.
 *
 * @author VISTALL
 */
public interface TService extends DomElement {

  /**
   * Returns the value of the servicefactory child.
   *
   * @return the value of the servicefactory child.
   */
  @NotNull
  GenericAttributeValue<Boolean> getServicefactory();


  /**
   * Returns the list of provide children.
   *
   * @return the list of provide children.
   */
  @NotNull
  @Required
  List<TProvide> getProvides();

  /**
   * Adds new child to the list of provide children.
   *
   * @return created child
   */
  TProvide addProvide();
}
