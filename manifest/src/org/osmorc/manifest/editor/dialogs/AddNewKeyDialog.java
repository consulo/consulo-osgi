package org.osmorc.manifest.editor.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.TextFieldWithAutoCompletion;
import org.jetbrains.annotations.Nullable;
import org.osmorc.manifest.lang.ManifestFileType;
import org.osmorc.manifest.lang.headerparser.HeaderParserEP;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;


/**
 * @author VISTALL
 * @since 13:16/03.05.13
 */
public class AddNewKeyDialog extends DialogWrapper {
  private JPanel myRoot;
  private final TextFieldWithAutoCompletion<String> myTextField;

  public AddNewKeyDialog(@Nullable Project project) {
    super(project);

    myRoot = new JPanel(new BorderLayout());

    HeaderParserEP[] extensions = HeaderParserEP.EP_NAME.getExtensions();
    List<String> list = new ArrayList<String>(extensions.length - 1);
    for (HeaderParserEP ep : extensions) {
      if (ep.key.isEmpty()) {
        continue;
      }
      list.add(ep.key);
    }
    myTextField = new TextFieldWithAutoCompletion<String>(project,
                                                                                            new TextFieldWithAutoCompletion.StringsCompletionProvider(
                                                                                              list, ManifestFileType.INSTANCE.getIcon()),
                                                                                            true, null);
    myRoot.add(myTextField, BorderLayout.CENTER);
    setTitle("Add New Key");
    init();
    pack();
  }

  public String getKey() {
    return myTextField.getText();
  }

  @Nullable
  @Override
  public JComponent getPreferredFocusedComponent() {
    return myTextField;
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    return myRoot;
  }
}
