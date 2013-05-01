package org.jetbrains.osgi;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 17:25/27.04.13
 */
public interface OSGiIcons {
  Icon FacetType = IconLoader.findIcon("/org/jetbrains/osgi/icons/osgiFacet.png");
  Icon ExportPackage = IconLoader.findIcon("/org/jetbrains/osgi/icons/exportPackage.png");
  Icon ImportPackage = IconLoader.findIcon("/org/jetbrains/osgi/icons/importPackage.png");
  Icon OsgiComponentFile = IconLoader.findIcon("/org/jetbrains/osgi/icons/osgiComponentFile.png");
  Icon OsgiComponent = IconLoader.findIcon("/org/jetbrains/osgi/icons/osgiComponent.png");
  Icon OsgiBundleActivator = IconLoader.findIcon("/org/jetbrains/osgi/icons/osgiBundleActivator.png");
}
