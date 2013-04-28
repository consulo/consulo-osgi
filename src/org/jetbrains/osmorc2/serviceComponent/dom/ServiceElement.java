package org.jetbrains.osmorc2.serviceComponent.dom;

import com.intellij.util.xml.DomElement;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 14:40/28.04.13
 */
public interface ServiceElement extends DomElement {
  @Nullable
  ProvideElement getProvide();
}
