package org.jetbrains.osmorc2.serviceComponent.dom;

import com.intellij.util.xml.DefinesXml;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author VISTALL
 * @since 14:14/28.04.13
 */
@DefinesXml
public interface ComponentElement extends DomElement {
  @NotNull
  @Required
  GenericAttributeValue<String> getName();

  @NotNull
  GenericAttributeValue<Boolean> isImmediate();

  @NotNull
  List<PropertyElement> getProperties();

  @Nullable
  ImplementationElement getImplementation();

  @Nullable
  ServiceElement getService();
}
