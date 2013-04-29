// Generated on Mon Apr 29 12:20:26 EEST 2013
// DTD/Schema  :    http://www.osgi.org/xmlns/scr/v1.1.0

package org.jetbrains.osmorc2.serviceComponent.dom;

/**
 * http://www.osgi.org/xmlns/scr/v1.1.0:Tjava-types enumeration.
 *
 * @author VISTALL
 */
public enum TJavaTypes implements com.intellij.util.xml.NamedEnum {
  BOOLEAN("Boolean"),
  BYTE("Byte"),
  CHARACTER("Character"),
  DOUBLE("Double"),
  FLOAT("Float"),
  INTEGER("Integer"),
  LONG("Long"),
  SHORT("Short"),
  STRING("String");

  private final String value;

  private TJavaTypes(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
