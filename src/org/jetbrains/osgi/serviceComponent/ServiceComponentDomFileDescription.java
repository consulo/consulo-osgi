package org.jetbrains.osgi.serviceComponent;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osgi.OSGiIcons;
import org.jetbrains.osgi.facet.OSGiFacetUtil;
import org.jetbrains.osgi.serviceComponent.dom.TComponent;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 14:16/28.04.13
 */
public class ServiceComponentDomFileDescription extends DomFileDescription<TComponent> {
  public ServiceComponentDomFileDescription() {
    super(TComponent.class, "component");
    registerNamespacePolicy("scr", "http://www.osgi.org/xmlns/scr/v1.1.0");
  }

  @Override
  public boolean isMyFile(@NotNull XmlFile file, @Nullable Module module) {
    final VirtualFile virtualFile = file.getVirtualFile();
    if(virtualFile == null) {
      return false;
    }
    if(module == null) {
      module = ModuleUtil.findModuleForFile(virtualFile, file.getProject());
    }

    if(module == null) {
      return false;
    }

    VirtualFile osGiInf = OSGiFacetUtil.getOSGiInf(module);
    return osGiInf != null && VfsUtilCore.isAncestor(osGiInf, virtualFile, false);
  }

  @Nullable
  @Override
  public Icon getFileIcon(@Iconable.IconFlags int flags) {
    return OSGiIcons.OsgiComponentFile;
  }
}