package org.jetbrains.osmorc2.ide.codeInspection;

import com.intellij.util.xml.highlighting.BasicDomElementsInspection;
import org.jetbrains.osmorc2.serviceComponent.dom.ComponentElement;

/**
 * @author VISTALL
 * @since 14:34/28.04.13
 */
public class ServiceComponentInspection extends BasicDomElementsInspection<ComponentElement> {

  public ServiceComponentInspection() {
    super(ComponentElement.class);
  }
}
