import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class AntlrSelectListener extends PlSqlParserBaseListener {

    @Override
    public void exitSelected_element(PlSqlParser.Selected_elementContext ctx) {

        System.out.println("Exit Columns " + ctx.getText());
    }

    @Override
    public void enterSelected_element(PlSqlParser.Selected_elementContext ctx) {

        System.out.println("Enter Columns " + ctx.getText());
    }

    public void printSelect(String selectSentence) {
        // Get our lexer
        PlSqlLexer lexer = new PlSqlLexer(new ANTLRInputStream(selectSentence));

        // Get a list of matched tokens
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Pass the tokens to the parser
        PlSqlParser parser = new PlSqlParser(tokens);

        // Specify our entry point
        PlSqlParser.Select_statementContext selectSentenceContext = parser.select_statement();

        // Walk it and attach our listener
        ParseTreeWalker walker = new ParseTreeWalker();
        AntlrSelectListener listener = new AntlrSelectListener();
        walker.walk(listener, selectSentenceContext);
    }

}
