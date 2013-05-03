package org.osmorc.manifest.editor;

import org.osmorc.manifest.lang.psi.Header;
import org.osmorc.manifest.lang.psi.ManifestFile;

import javax.swing.AbstractListModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 12:52/03.05.13
 */
public class ManifestEditorListModel extends AbstractListModel<String> {
  private final ManifestFile myManifestFile;
  private List<String> myList;

  public ManifestEditorListModel(ManifestFile manifestFile) {
    myManifestFile = manifestFile;
  }

  @Override
  public int getSize() {
    List<String> value = getValue();
    return value.size();
  }

  @Override
  public String getElementAt(int index) {
    List<String> value = getValue();

    return value.get(index);
  }

  private List<String> getValue() {
    if (myList == null) {
      Header[] headers = myManifestFile.getHeaders();
      List<String> list = new ArrayList<String>(headers.length);
      for (Header h : headers) {
        list.add(h.getName());
      }
      myList = list;
    }

    return myList;
  }

  public void setInvalidData() {
    int size = getSize();

    myList = null;

    fireContentsChanged(this, 0, size);
  }
}
