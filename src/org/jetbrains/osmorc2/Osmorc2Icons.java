package org.jetbrains.osmorc2;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 17:25/27.04.13
 */
public interface Osmorc2Icons {
  Icon FacetType = IconLoader.findIcon("/org/jetbrains/osmorc2/icons/osmorc2Facet.png");
  Icon BundleActivator = FacetType;     //TODO [VISTALL]
  Icon ExportPackage = IconLoader.findIcon("/org/jetbrains/osmorc2/icons/exportPackage.png");
  Icon ImportPackage = IconLoader.findIcon("/org/jetbrains/osmorc2/icons/importPackage.png");
  Icon ServiceFile = IconLoader.findIcon("/org/jetbrains/osmorc2/icons/serviceFile.png");
}
