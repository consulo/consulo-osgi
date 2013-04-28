package org.jetbrains.osmorc2.serviceComponent.dom;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 14:27/28.04.13
 */
public interface PropertyElement extends DomElement {
  @NotNull
  @Required
  GenericAttributeValue<String> getName();

  @NotNull
  @Required
  GenericAttributeValue<String> getValue();
}
