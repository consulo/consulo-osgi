package org.jetbrains.osgi.ide.codeInspection;

import com.intellij.util.xml.highlighting.BasicDomElementsInspection;
import org.jetbrains.osgi.serviceComponent.dom.TComponent;

/**
 * @author VISTALL
 * @since 14:34/28.04.13
 */
public class ServiceComponentResolveInspection extends BasicDomElementsInspection<TComponent> {

  public ServiceComponentResolveInspection() {
    super(TComponent.class);
  }
}
