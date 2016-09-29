// Generated on Mon Apr 29 12:20:26 EEST 2013
// DTD/Schema  :    http://www.osgi.org/xmlns/scr/v1.1.0

package consulo.osgi.serviceComponent.dom;

/**
 * http://www.osgi.org/xmlns/scr/v1.1.0:Tconfiguration-policy enumeration.
 *
 * @author VISTALL
 */
public enum TConfigurationPolicy implements com.intellij.util.xml.NamedEnum
{
	IGNORE("ignore"),
	OPTIONAL("optional"),
	REQUIRE("require");

	private final String value;

	private TConfigurationPolicy(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}

}
