package consulo.osgi;

import com.intellij.openapi.util.IconLoader;
import consulo.ui.image.Image;

/**
 * @author VISTALL
 * @since 17:25/27.04.13
 */
public interface OSGiIcons
{
	Image FacetType = IconLoader.findIcon("/org/jetbrains/osgi/icons/osgiFacet.png");
	Image ExportPackage = IconLoader.findIcon("/org/jetbrains/osgi/icons/exportPackage.png");
	Image ImportPackage = IconLoader.findIcon("/org/jetbrains/osgi/icons/importPackage.png");
	Image OsgiComponentFile = IconLoader.findIcon("/org/jetbrains/osgi/icons/osgiComponentFile.png");
	Image OsgiComponentImplementation = IconLoader.findIcon("/org/jetbrains/osgi/icons/osgiComponentImplementation.png");
	Image OsgiComponentInterface = IconLoader.findIcon("/org/jetbrains/osgi/icons/osgiComponentInterface.png");
	Image OsgiBundleActivator = IconLoader.findIcon("/org/jetbrains/osgi/icons/osgiBundleActivator.png");
}
