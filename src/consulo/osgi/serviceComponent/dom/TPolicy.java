// Generated on Mon Apr 29 12:20:26 EEST 2013
// DTD/Schema  :    http://www.osgi.org/xmlns/scr/v1.1.0

package consulo.osgi.serviceComponent.dom;

/**
 * http://www.osgi.org/xmlns/scr/v1.1.0:Tpolicy enumeration.
 *
 * @author VISTALL
 */
public enum TPolicy implements com.intellij.util.xml.NamedEnum
{
	DYNAMIC("dynamic"),
	STATIC("static");

	private final String value;

	private TPolicy(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}

}
