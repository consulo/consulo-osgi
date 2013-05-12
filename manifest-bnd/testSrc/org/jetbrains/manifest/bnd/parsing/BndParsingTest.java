package org.jetbrains.manifest.bnd.parsing;

import com.intellij.testFramework.ParsingTestCase;
import org.jetbrains.manifest.bnd.lang.BndParsingDefinition;
import org.osmorc.manifest.lang.ManifestParserDefinition;
import org.osmorc.manifest.lang.headerparser.HeaderParserEP;
import org.osmorc.manifest.lang.headerparser.impl.GenericComplexHeaderParser;

/**
 * @author VISTALL
 * @since 14:29/12.05.13
 */
public class BndParsingTest extends ParsingTestCase {
  public BndParsingTest() {
    super("parsing", "bnd", new BndParsingDefinition(), new ManifestParserDefinition());
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    registerExtensionPoint(HeaderParserEP.EP_NAME, HeaderParserEP.class);

    registerExtension(HeaderParserEP.EP_NAME, new HeaderParserEP("", GenericComplexHeaderParser.class));
  }

  public void testComment() {
    doTest(true);
  }

  @Override
  protected String getTestDataPath() {
    return "manifest-bnd/testData";
  }

  @Override
  protected boolean shouldContainTempFiles() {
    return false;
  }
}
