// Generated on Mon Apr 29 12:20:26 EEST 2013
// DTD/Schema  :    http://www.osgi.org/xmlns/scr/v1.1.0

package org.jetbrains.osgi.serviceComponent.dom;

/**
 * http://www.osgi.org/xmlns/scr/v1.1.0:Tcardinality enumeration.
 *
 * @author VISTALL
 */
public enum TCardinality implements com.intellij.util.xml.NamedEnum {
  Tcardinality_0__1("0..1"),
  Tcardinality_0__N("0..n"),
  Tcardinality_1__1("1..1"),
  Tcardinality_1__N("1..n");

  private final String value;

  private TCardinality(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
