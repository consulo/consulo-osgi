package org.jetbrains.osgi.facet.validation;

import com.intellij.facet.ui.FacetConfigurationQuickFix;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.packaging.artifacts.Artifact;
import com.intellij.packaging.artifacts.ArtifactManager;
import com.intellij.packaging.artifacts.ArtifactTemplate;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.osgi.compiler.artifact.OSGiArtifactTemplate;
import org.jetbrains.osgi.compiler.artifact.OSGiArtifactType;
import org.jetbrains.osgi.facet.OSGiFacet;

import javax.swing.JComboBox;
import javax.swing.JComponent;

/**
 * @author VISTALL
 * @since 23:59/11.05.13
 */
public class OSGiCreateArtifactQuickFix extends FacetConfigurationQuickFix {
  private final OSGiFacet myFacet;
  private final JComboBox<Artifact> myComboBox;

  public OSGiCreateArtifactQuickFix(OSGiFacet facet, JComboBox<Artifact> comboBox) {
    myFacet = facet;
    myComboBox = comboBox;
  }

  @Override
  public void run(JComponent place) {
    final ArtifactTemplate.NewArtifactConfiguration template = OSGiArtifactTemplate.doCreateArtifactTemplate(myFacet);

    final Project project = myFacet.getModule().getProject();
    ArtifactManager manager = ArtifactManager.getInstance(project);

    Artifact artifact = manager.findArtifact(template.getArtifactName());
    if(artifact != null) {
      if(!(artifact.getArtifactType() instanceof OSGiArtifactType)) {
        Messages.showErrorDialog(place, "Artifact had invalid type", "Wrong Artifact");
        return;
      }

      Messages.showWarningDialog(place, "Artifact already exists - selected", "Already Exists");
    }
    else {
      artifact = manager.addArtifact(template.getArtifactName(), template.getArtifactType(), template.getRootElement());
    }

    final Artifact tempArtifact = artifact;
    UIUtil.invokeLaterIfNeeded(new Runnable() {
      @Override
      public void run() {
        myComboBox.removeAllItems();
        for (Artifact temp : ArtifactManager.getInstance(project).getSortedArtifacts()) {
          if (temp.getArtifactType() instanceof OSGiArtifactType) {
            myComboBox.addItem(temp);
          }
        }
        myComboBox.setSelectedItem(tempArtifact);
      }
    });
  }
}
