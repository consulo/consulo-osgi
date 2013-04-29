// Generated on Mon Apr 29 12:20:26 EEST 2013
// DTD/Schema  :    http://www.osgi.org/xmlns/scr/v1.1.0

package org.jetbrains.osmorc2.serviceComponent.dom;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.*;
import com.intellij.util.xml.converters.ClassValueConverterImpl;
import org.jetbrains.annotations.NotNull;

/**
 * http://www.osgi.org/xmlns/scr/v1.1.0:Timplementation interface.
 *
 * @author VISTALL
 */
public interface Timplementation extends DomElement {

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
