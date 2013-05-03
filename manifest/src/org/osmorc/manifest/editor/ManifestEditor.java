package org.osmorc.manifest.editor;

import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.ide.actions.QualifiedNameProvider;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProviders;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osmorc.manifest.editor.dialogs.AddNewKeyDialog;
import org.osmorc.manifest.editor.models.ClauseTableModel;
import org.osmorc.manifest.editor.models.HeaderListModel;
import org.osmorc.manifest.lang.headerparser.HeaderParser;
import org.osmorc.manifest.lang.headerparser.HeaderUtil;
import org.osmorc.manifest.lang.psi.Clause;
import org.osmorc.manifest.lang.psi.Header;
import org.osmorc.manifest.lang.psi.HeaderValuePart;
import org.osmorc.manifest.lang.psi.ManifestFile;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author VISTALL
 * @since 12:33/03.05.13
 */
public class ManifestEditor extends UserDataHolderBase implements FileEditor {
  private static class MyTextFieldWithAutoCompletionListProvider extends TextFieldWithAutoCompletionListProvider<Object> {

    private final Header myHeaderByName;
    private final HeaderParser myHeaderParser;

    public MyTextFieldWithAutoCompletionListProvider(Header headerByName, HeaderParser headerParser) {
      super(null);
      myHeaderByName = headerByName;
      myHeaderParser = headerParser;
    }

    @NotNull
    @Override
    public Collection<Object> getItems(String prefix, boolean cached, CompletionParameters parameters) {
      return ApplicationManager.getApplication().runReadAction(new Computable<Collection<Object>>() {
        @Override
        public Collection<Object> compute() {
          Clause[] clauses = myHeaderByName.getClauses();
          if (clauses.length == 0) {
            return Collections.emptyList();
          }
          HeaderValuePart value = clauses[0].getValue();
          if (value == null) {
            return Collections.emptyList();
          }

          List<Object> objects = new ArrayList<Object>();
          PsiReference[] references = myHeaderParser.getReferences(value);
          for (PsiReference reference : references) {
            for (Object o : reference.getVariants()) {
              if(myHeaderParser.isAcceptable(o)) {
                objects.add(o);
              }
            }
          }
          return objects;
        }
      });
    }

    @Nullable
    @Override
    protected Icon getIcon(@NotNull Object item) {
      if (item instanceof NavigationItem) {
        ItemPresentation itemPresentation = ItemPresentationProviders.getItemPresentation((NavigationItem)item);
        if (itemPresentation != null) {
          return itemPresentation.getIcon(false);
        }
      }
      return null;
    }

    @NotNull
    @Override
    protected String getLookupString(@NotNull Object item) {
      if (item instanceof PsiElement) {
        for (QualifiedNameProvider provider : Extensions.getExtensions(QualifiedNameProvider.EP_NAME)) {
          String result = provider.getQualifiedName((PsiElement)item);
          if (result != null) {
            return result;
          }
        }
      }
      return item.toString();
    }

    @Nullable
    @Override
    protected String getTailText(@NotNull Object item) {
      return null;
    }

    @Nullable
    @Override
    protected String getTypeText(@NotNull Object item) {
      return null;
    }

    @Override
    public int compare(Object item1, Object item2) {
      return 0;
    }
  }

  private final JPanel myRoot;
  private final Project myProject;
  private final VirtualFile myVirtualFile;
  private final JPanel myContentPanel;

  private final ManifestFile myManifestFile;

  public ManifestEditor(final Project project, VirtualFile file) {
    myProject = project;
    myVirtualFile = file;
    myRoot = new JPanel(new BorderLayout());
    myRoot.setBorder(IdeBorderFactory.createEmptyBorder(0, 5, 0, 0));

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
        new TextFieldWithAutoCompletion<Object>(myProject, new MyTextFieldWithAutoCompletionListProvider(headerByName, headerParser), false, null);

      Object simpleConvertedValue = headerByName.getSimpleConvertedValue();
      if(simpleConvertedValue != null) {
        text.setText(simpleConvertedValue.toString());
      }


      JPanel panel = new JPanel(new BorderLayout());
      panel.add(new JLabel("Value: "), BorderLayout.WEST);
      panel.add(text, BorderLayout.CENTER);
      myContentPanel.add(panel, BorderLayout.NORTH);
    }
    else {
      JBSplitter splitter = new JBSplitter(true);
      splitter.setSplitterProportionKey(getClass().getName() + "#" + key);

      final JBList valueList = new JBList(new HeaderListModel(headerByName));
      valueList.setCellRenderer(new ListCellRendererWithRightAlignedComponent() {
        @Override
        protected void customize(Object value) {
          Clause clause = (Clause) value;
          HeaderValuePart headerValuePart = clause.getValue();
          setLeftText(headerValuePart == null ? "" : headerValuePart.getUnwrappedText());
        }
      });
      ToolbarDecorator valueDecorator = ToolbarDecorator.createDecorator(valueList);
      splitter.setFirstComponent(valueDecorator.createPanel());

      final ClauseTableModel model = new ClauseTableModel();
      final JBTable propertiesList = new JBTable(model);
      valueList.addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
          Clause value = (Clause)valueList.getSelectedValue();
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
