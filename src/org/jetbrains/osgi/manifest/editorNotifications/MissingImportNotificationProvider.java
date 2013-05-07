package org.jetbrains.osgi.manifest.editorNotifications;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.EditorNotifications;
import org.jetbrains.annotations.Nullable;
import org.osmorc.manifest.lang.ManifestFileType;
import org.osmorc.manifest.lang.psi.ManifestFile;

/**
 * @author VISTALL
 * @since 12:07/07.05.13
 */
public class MissingImportNotificationProvider extends EditorNotifications.Provider<MissingImportNotificationPanel> {
  private static final Key<MissingImportNotificationPanel> KEY = Key.create("osgi-missing-import");
  private final Project myProject;

  public MissingImportNotificationProvider(Project project) {

    myProject = project;
  }

  @Override
  public Key<MissingImportNotificationPanel> getKey() {
    return KEY;
  }

  @Nullable
  @Override
  public MissingImportNotificationPanel createNotificationPanel(VirtualFile virtualFile, FileEditor fileEditor) {
    if(virtualFile.getFileType() != ManifestFileType.INSTANCE) {
      return null;
    }
    PsiManager manager = PsiManager.getInstance(myProject);
    final PsiFile file = manager.findFile(virtualFile);
    if(!(file instanceof ManifestFile)) {
      return null;
    }
    return null;
  }
}
