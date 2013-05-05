package org.osmorc.manifest.editor;

import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.ReadonlyStatusHandler;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.AbstractTableCellEditor;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osmorc.manifest.editor.dialogs.AddNewKeyDialog;
import org.osmorc.manifest.editor.models.ClauseTableModel;
import org.osmorc.manifest.editor.models.HeaderTableModel;
import org.osmorc.manifest.lang.headerparser.HeaderParser;
import org.osmorc.manifest.lang.headerparser.HeaderUtil;
import org.osmorc.manifest.lang.psi.Clause;
import org.osmorc.manifest.lang.psi.Header;
import org.osmorc.manifest.lang.psi.ManifestFile;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeListener;

/**
 * @author VISTALL
 * @since 12:33/03.05.13
 */
public class ManifestEditor extends UserDataHolderBase implements FileEditor {

  private final JPanel myRoot;
  private final Project myProject;
  private final VirtualFile myVirtualFile;
  private final JPanel myContentPanel;

  private final ManifestFile myManifestFile;
  private final boolean myIsReadonlyFile;

  public ManifestEditor(final Project project, VirtualFile file) {
    myProject = project;
    myVirtualFile = file;
    myRoot = new JPanel(new BorderLayout());
    myRoot.setBorder(IdeBorderFactory.createEmptyBorder(0, 5, 0, 0));
    myIsReadonlyFile = !ReadonlyStatusHandler.ensureFilesWritable(myProject, myVirtualFile);

    JBSplitter splitter = new JBSplitter();
    splitter.setSplitterProportionKey(getClass().getName());
    myContentPanel = new JPanel(new BorderLayout());
    myContentPanel.setBorder(IdeBorderFactory.createEmptyBorder(5));

    Document document = FileDocumentManager.getInstance().getDocument(file);

    assert document != null;

    PsiFile psiFile = PsiManager.getInstance(project).findFile(file);

    assert psiFile instanceof ManifestFile;

    myManifestFile = (ManifestFile)psiFile;

    final ManifestEditorListModel dataModel = new ManifestEditorListModel(myManifestFile);

    final JBList list = new JBList(dataModel);
    list.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        final String selectedValue = (String)list.getSelectedValue();
        selectHeader(selectedValue);
      }
    });

    ToolbarDecorator decorator = ToolbarDecorator.createDecorator(list);
    decorator.setAddAction(new AnActionButtonRunnable() {
      @Override
      public void run(AnActionButton anActionButton) {
        AddNewKeyDialog dialog = new AddNewKeyDialog(project);
        boolean b = dialog.showAndGet();
        if (b) {
          final String key = dialog.getKey();
          if (!StringUtil.isEmptyOrSpaces(key)) {
            ApplicationManager.getApplication().runWriteAction(new Runnable() {
              @Override
              public void run() {
                myManifestFile.setHeaderValue(key, "");
              }
            });
          }
        }
      }
    });
    decorator.setRemoveAction(new AnActionButtonRunnable() {
      @Override
      public void run(AnActionButton anActionButton) {
        final String selectedValue = (String)list.getSelectedValue();
        if(selectedValue == null) {
          return;
        }
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
          @Override
          public void run() {
            final Header headerByName = myManifestFile.getHeaderByName(selectedValue);
            if(headerByName != null) {
              headerByName.delete();
            }
          }
        });
      }
    });
    disableActionsIfNeed(decorator);

    splitter.setFirstComponent(decorator.createPanel());
    splitter.setSecondComponent(myContentPanel);

    myRoot.add(splitter);

    document.addDocumentListener(new com.intellij.openapi.editor.event.DocumentAdapter() {
      @Override
      public void documentChanged(DocumentEvent event) {
        dataModel.setInvalidData();
      }
    });
  }

  public void selectHeader(@Nullable String key) {
    myContentPanel.removeAll();

    if (key == null) {
      return;
    }

    final Header headerByName = myManifestFile.getHeaderByName(key);
    if (headerByName == null) {
      return;
    }

    final HeaderParser headerParser = HeaderUtil.getHeaderParser(key);
    if (headerParser.isSimpleHeader()) {
      TextFieldWithAutoCompletion<Object> text =
        new TextFieldWithAutoCompletion<Object>(myProject, new MyTextFieldCompletionProvider(headerByName, headerParser), false, null);

      text.setOneLineMode(false);
      text.setEnabled(!myIsReadonlyFile);
      Object simpleConvertedValue = headerByName.getSimpleConvertedValue();
      if(simpleConvertedValue != null) {
        text.setText(simpleConvertedValue.toString());
      }

      myContentPanel.add(new JBScrollPane(text), BorderLayout.CENTER);
    }
    else {
      JBSplitter splitter = new JBSplitter(true);
      splitter.setSplitterProportionKey(getClass().getName() + "#" + key);

      final HeaderTableModel valueListModel = new HeaderTableModel(headerByName, headerParser, myIsReadonlyFile);
      final JBTable valueList = new JBTable(valueListModel);
      valueList.getColumnModel().getColumn(0).setCellEditor(new AbstractTableCellEditor() {

        private TextFieldWithAutoCompletion<Object> myTextField;

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
          myTextField =
            new TextFieldWithAutoCompletion<Object>(myProject, new MyTextFieldCompletionProvider(headerByName, headerParser),

                                                      false, null);
          myTextField.setText((String)value);
          return myTextField;
        }

        @Override
        public Object getCellEditorValue() {
          return myTextField.getText();
        }
      });
      valueList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

      ToolbarDecorator valueDecorator = ToolbarDecorator.createDecorator(valueList);
      disableActionsIfNeed(valueDecorator);

      splitter.setFirstComponent(valueDecorator.createPanel());

      final ClauseTableModel model = new ClauseTableModel(myIsReadonlyFile);
      final JBTable propertiesList = new JBTable(model);
      propertiesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

      valueList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
          Clause value = valueListModel.getRowValue(e.getFirstIndex());
          model.setClause(value);
          /*UIUtil.invokeAndWaitIfNeeded(new Runnable() {
            @Override
            public void run() {
              propertiesList.revalidate();
              propertiesList.repaint();
            }
          });*/
        }
      });
      ToolbarDecorator propertiesDecorator = ToolbarDecorator.createDecorator(propertiesList);
      disableActionsIfNeed(propertiesDecorator);

      splitter.setSecondComponent(propertiesDecorator.createPanel());

      myContentPanel.add(splitter, BorderLayout.CENTER);
    }

    UIUtil.invokeAndWaitIfNeeded(new Runnable() {
      @Override
      public void run() {
        myContentPanel.revalidate();
        myContentPanel.repaint();
      }
    });
  }

  private void disableActionsIfNeed(ToolbarDecorator toolbarDecorator) {
    toolbarDecorator.disableUpDownActions();
    if(myIsReadonlyFile) {
      toolbarDecorator.disableUpDownActions();
      toolbarDecorator.disableAddAction();
      toolbarDecorator.disableRemoveAction();
    }
  }

  @NotNull
  @Override
  public JComponent getComponent() {
    return myRoot;
  }

  @Nullable
  @Override
  public JComponent getPreferredFocusedComponent() {
    return myRoot;
  }

  @NotNull
  @Override
  public String getName() {
    return "UI Editor";
  }

  @NotNull
  @Override
  public FileEditorState getState(@NotNull FileEditorStateLevel level) {
    return FileEditorState.INSTANCE;
  }

  @Override
  public void setState(@NotNull FileEditorState state) {
  }

  @Override
  public boolean isModified() {
    return false;
  }

  @Override
  public boolean isValid() {
    return myVirtualFile.isValid();
  }

  @Override
  public void selectNotify() {
  }

  @Override
  public void deselectNotify() {
  }

  @Override
  public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
  }

  @Override
  public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
  }

  @Nullable
  @Override
  public BackgroundEditorHighlighter getBackgroundHighlighter() {
    return null;
  }

  @Nullable
  @Override
  public FileEditorLocation getCurrentLocation() {
    return null;
  }

  @Nullable
  @Override
  public StructureViewBuilder getStructureViewBuilder() {
    return null;
  }

  @Override
  public void dispose() {
  }
}
