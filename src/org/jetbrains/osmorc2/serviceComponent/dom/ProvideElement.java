package org.jetbrains.osmorc2.serviceComponent.dom;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.converters.ClassValueConverterImpl;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 14:41/28.04.13
 */
public interface ProvideElement extends DomElement {
  @NotNull
  @Convert(value = ClassValueConverterImpl.class, soft = false)
  GenericAttributeValue<PsiClass> getInterface();
}
