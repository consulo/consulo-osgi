package consulo.osgi;

import javax.swing.Icon;

import com.intellij.openapi.util.IconLoader;
import consulo.ui.image.Image;

/**
 * @author VISTALL
 * @since 17:25/27.04.13
 */
public interface OSGiIcons
{
	Image FacetType = IconLoader.findIcon("/org/jetbrains/osgi/icons/osgiFacet.png");
	Icon ExportPackage = IconLoader.findIcon("/org/jetbrains/osgi/icons/exportPackage.png");
	Icon ImportPackage = IconLoader.findIcon("/org/jetbrains/osgi/icons/importPackage.png");
	Icon OsgiComponentFile = IconLoader.findIcon("/org/jetbrains/osgi/icons/osgiComponentFile.png");
	Icon OsgiComponentImplementation = IconLoader.findIcon("/org/jetbrains/osgi/icons/osgiComponentImplementation.png");
	Icon OsgiComponentInterface = IconLoader.findIcon("/org/jetbrains/osgi/icons/osgiComponentInterface.png");
	Icon OsgiBundleActivator = IconLoader.findIcon("/org/jetbrains/osgi/icons/osgiBundleActivator.png");
}
