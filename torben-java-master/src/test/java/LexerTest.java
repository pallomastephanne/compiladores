import error.CompilerError;
import io.vavr.collection.List;
import java_cup.runtime.Symbol;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import parse.Lexer;
import parse.Terminals;

import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertArrayEquals;

public class LexerTest {

   private String run(String input) throws IOException {
      Lexer lexer = new Lexer(new StringReader(input), "unknown");
      Symbol token;
      StringBuilder builder = new StringBuilder();
      List<String> list = List.empty();
      do {
         token = lexer.next_token();
         builder.append(Terminals.dumpTerminal(token)).append('\n');
         list = list.append(Terminals.dumpTerminal(token));
      } while (token.sym != Terminals.EOF);
      return builder.toString();
      //return list;
   }

   private void trun(String input, String... output) throws IOException {
      StringBuilder builder = new StringBuilder();
      for (String x : output)
         builder.append(x).append('\n');
      softly.assertThat(run(input))
         .as("%s", input)
         .isEqualTo(builder.toString());
   }

   private void erun(String input, String message) throws IOException {
      softly.assertThatThrownBy(() -> run(input))
         .as("%s", input)
         .isInstanceOf(CompilerError.class)
         .hasMessage(message);
   }

   @Rule
   public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

   @Test
   public void lexerTest1() throws IOException {

      // punctuation
      trun("(", "1:1-1:2 LPAREN", "1:2-1:2 EOF");
      trun(")", "1:1-1:2 RPAREN", "1:2-1:2 EOF");

      // operators
      trun("+", "1:1-1:2 PLUS", "1:2-1:2 EOF");
      trun("<", "1:1-1:2 LT", "1:2-1:2 EOF");

      // boolean literals
      trun("true", "1:1-1:5 LITBOOL(true)", "1:5-1:5 EOF");
      trun("false", "1:1-1:6 LITBOOL(false)", "1:6-1:6 EOF");

      // integer literals
      trun("26342", "1:1-1:6 LITINT(26342)", "1:6-1:6 EOF");
      trun("0", "1:1-1:2 LITINT(0)", "1:2-1:2 EOF");
      //trun("+75"    , "1:1-1:4 LITINT(75)"    , "1:4-1:4 EOF");
      //trun("-75"    , "1:1-1:4 LITINT(-75)"   , "1:4-1:4 EOF");


      // keywords
      trun("bool", "1:1-1:5 BOOL", "1:5-1:5 EOF");
      trun("int", "1:1-1:4 INT", "1:4-1:4 EOF");
      trun("if", "1:1-1:3 IF", "1:3-1:3 EOF");
      trun("then", "1:1-1:5 THEN", "1:5-1:5 EOF");
      trun("else", "1:1-1:5 ELSE", "1:5-1:5 EOF");
      trun("let", "1:1-1:4 LET", "1:4-1:4 EOF");
      trun("in", "1:1-1:3 IN", "1:3-1:3 EOF");

      // identifiers
      trun("nome", "1:1-1:5 ID(nome)", "1:5-1:5 EOF");
      trun("camelCase", "1:1-1:10 ID(camelCase)", "1:10-1:10 EOF");
      trun("with_underscore", "1:1-1:16 ID(with_underscore)", "1:16-1:16 EOF");
      trun("A1b2C33", "1:1-1:8 ID(A1b2C33)", "1:8-1:8 EOF");
      trun("set+", "1:1-1:4 ID(set)", "1:4-1:5 PLUS", "1:5-1:5 EOF");
      trun("45let", "1:1-1:3 LITINT(45)", "1:3-1:6 LET", "1:6-1:6 EOF");
      erun("_invalid", "1:1-1:2 lexical error: invalid character '_'");
   }

}
