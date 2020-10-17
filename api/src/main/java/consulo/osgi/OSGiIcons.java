package consulo.osgi;

import consulo.osgi.icon.OSGiIconGroup;
import consulo.ui.image.Image;

/**
 * @author VISTALL
 * @since 17:25/27.04.13
 */
public interface OSGiIcons
{
	Image FacetType = OSGiIconGroup.osgiFacet();
	Image ExportPackage = OSGiIconGroup.exportPackage();
	Image ImportPackage = OSGiIconGroup.importPackage();
	Image OsgiComponentFile = OSGiIconGroup.osgiComponentFile();
	Image OsgiComponentImplementation = OSGiIconGroup.osgiComponentImplementation();
	Image OsgiComponentInterface = OSGiIconGroup.osgiComponentInterface();
	Image OsgiBundleActivator = OSGiIconGroup.osgiBundleActivator();
}
