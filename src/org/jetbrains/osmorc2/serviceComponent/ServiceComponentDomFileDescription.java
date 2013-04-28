package org.jetbrains.osmorc2.serviceComponent;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osmorc2.Osmorc2Icons;
import org.jetbrains.osmorc2.serviceComponent.dom.ComponentElement;
import org.osmorc.facet.OsmorcFacetUtil;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 14:16/28.04.13
 */
public class ServiceComponentDomFileDescription extends DomFileDescription<ComponentElement> {
  public ServiceComponentDomFileDescription() {
    super(ComponentElement.class, "component");
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
    final VirtualFile parent = virtualFile.getParent();
    return parent.getName().equals("OSGI-INF") && module != null && OsmorcFacetUtil.hasOsmorcFacet(module);
  }

  @Nullable
  @Override
  public Icon getFileIcon(@Iconable.IconFlags int flags) {
    return Osmorc2Icons.ServiceFile;
  }
}
