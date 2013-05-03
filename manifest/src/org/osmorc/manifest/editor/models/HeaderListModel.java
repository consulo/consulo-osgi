package org.osmorc.manifest.editor.models;

import org.osmorc.manifest.lang.psi.Clause;
import org.osmorc.manifest.lang.psi.Header;

import java.util.AbstractList;

/**
 * @author VISTALL
 * @since 15:47/03.05.13
 */
public class HeaderListModel extends AbstractList<Clause>{
  private final Header myHeader;

  public HeaderListModel(Header header) {
    myHeader = header;
  }

  @Override
  public Clause get(int index) {
    return myHeader.getClauses()[index];
  }

  @Override
  public int size() {
    return myHeader.getClauses().length;
  }
}
