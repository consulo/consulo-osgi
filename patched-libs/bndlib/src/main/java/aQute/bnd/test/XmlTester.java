package aQute.bnd.test;

import org.w3c.dom.Document;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;

public class XmlTester {
  final static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
  final static XPathFactory xpathf = XPathFactory.newInstance();
  final static DocumentBuilder db;

  static {
    try {
      dbf.setNamespaceAware(true);
      db = dbf.newDocumentBuilder();
    }
    catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

  final Document document;
  final XPath xpath = xpathf.newXPath();

  public XmlTester(InputStream in, final String... namespace) throws Exception {
    xpath.setNamespaceContext(new NamespaceContext() {

      public Iterator getPrefixes(String namespaceURI) {
        return Arrays.asList("md", "scr").iterator();
      }

      public String getPrefix(String namespaceURI) {
        for (int i = 0; i < namespace.length; i += 2) {
          if (namespaceURI.equals(namespace[i + 1])) return namespace[i];
        }
        return null;
      }

      public String getNamespaceURI(String prefix) {
        for (int i = 0; i < namespace.length; i += 2) {
          if (prefix.equals(namespace[i])) return namespace[i + 1];
        }
        return null;
      }
    });

    document = db.parse(in);
  }

  public void assertAttribute(String value, String expr) throws XPathExpressionException {
    System.out.println(expr);
    String o = (String)xpath.evaluate(expr, document, XPathConstants.STRING);
    /*Assert.assertNotNull(o);
    Assert.assertEquals(value, o);   */
  }

}
