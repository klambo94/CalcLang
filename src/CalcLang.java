import java.util.Scanner;

/**
 * CalcLang Project.
 * Main taken from Otter's Parser.java from Professor Schultz.
 */
public class CalcLang {
    public static void main(String[] args) throws Exception {
        System.out.print("Enter file name: ");
        Scanner keys = new Scanner( System.in );
        String name = keys.nextLine();
        Lexer lex = new Lexer( name );
        Parser parser = new Parser( lex );

        Node root = parser.parseStatements();
        root.executeNode();
       // TreeViewer viewer = new TreeViewer("Parse Tree", 0, 0, 800, 500, root );

    }
}
