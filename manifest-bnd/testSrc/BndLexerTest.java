import com.intellij.psi.tree.IElementType;
import org.jetbrains.manifest.bnd.lang.BndLexer;

/**
 * @author VISTALL
 * @since 14:02/12.05.13
 */
public class BndLexerTest {
  public static void main(String[] args) {
    BndLexer lexer = new BndLexer();

    String str = "#       this is comment\nHeader: test\n";

    lexer.start(str);

    IElementType type;
    while ((type = lexer.getTokenType()) != null)  {
      System.out.println(type);
      lexer.advance();
    }
  }
}
