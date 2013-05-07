package org.jetbrains.osgi.manifest.editorNotifications;

import com.intellij.ui.EditorNotificationPanel;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 12:07/07.05.13
 */
public class MissingImportNotificationPanel extends EditorNotificationPanel {
  public MissingImportNotificationPanel(@NotNull String packageName) {
    setText("Missing package '" + packageName + "' in 'Import-Package' header");
  }
}
