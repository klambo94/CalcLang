/*
    This class provides a recursive descent parser of CalcLang,
    creating a parse tree. Taken from Otter's Parser which is created by
    Schultz.
*/


public class Parser {

    private Lexer lex;

    public Parser( Lexer lexer ) {
        lex = lexer;
    }

    public Node parseStatements() throws Exception {
        System.out.println("----------------------> Parsing Statements");
        Token token = lex.getToken();
        Node first = null;
        Node second = null;

        if(!token.isKind("eof")){
            first = parseStatement();
            second = parseStatements();
        }
        return new StatementsNode("Statements", first, second);
    }

    public Node parseStatement() throws Exception {
        System.out.println("----------------------> Parsing Statement");
        Node first = null;
        Token token = lex.getToken();


        if(token.isKind("id")) {
            lex.putBack(token);
           first = createAssignmentNode();
        } else if(token.isKind("keyword")) {
            token = lex.getToken();
            first = createKeyWordNode();
        } else {
            lex.putBack(token);
        }
        return new StatementNode("Statements" , first);

    }

    private Node createKeyWordNode () throws Exception {
        System.out.println("----------------------> Creating Display Node");
        Token token = lex.getToken();
        String tokenDetails = token.getDetails();

        if(tokenDetails.equals("show")
                || tokenDetails.equals("msg")
                || tokenDetails.equals("newline")){


            if(tokenDetails.equals("msg")){
                token = lex.getToken();
                return new DisplayNode(token);
            } else if(tokenDetails.equals("show")) {
                Node first = parseExpression();
                return new DisplayNode("show", first);
            } else {
                return  new DisplayNode(token);
            }

        } else if(tokenDetails.equals("input")){
            Token displayString = lex.getToken();
            Token inputVar = lex.getToken();

            return  new InputNode("Input", new StatementNode(displayString), new StatementNode(inputVar));
        } else {
            throw new Exception("Reached an unknown keyword: " + tokenDetails);
        }
    }

    private Node createAssignmentNode () throws Exception {
        System.out.println("----------------------> Creating Assignment Node");
        Token token = lex.getToken();
        String token2Details = lex.getToken().getDetails();
        if(token2Details.equals("=")){
          Node first = new StatementNode(token);
          Node second = parseExpression();

            return new AssignmentNode("Assignment", first, second);
        } else {
            throw new Exception("Unknown id case: " + token2Details);
        }
    }

    private Node parseExpression () {
        System.out.println("----------------------> Parsing Expression");
        Token token = lex.getToken();
        Node first = null;
        Node[] children;
        if(token.isKind("id") || token.isKind("digit")){
            Token nextToken = lex.getToken();
            if(nextToken.getDetails().equals("+")||
                    nextToken.getDetails().equals("-")){
                lex.putBack(nextToken);
                lex.putBack(token);

                children = parseExpressionOperation();
            } else {
                lex.putBack(token);
                lex.putBack(nextToken);
                parseTerm(); //TODO: Save this into a variable not sure what yet.
            }
        }

        return new ExpressionNode(null);
    }

    private Node[] parseExpressionOperation() {
        Node first= parseTerm();
        Token secondToken = lex.getToken();
        Node second = new StatementNode(secondToken);
        Node third = parseExpression();

        return new Node[] {first, second, third};
    }

    private Node parseTerm() {
        Token token = lex.getToken();
        Token nextToken = lex.getToken();

        if(nextToken.getDetails().equals("*")
                || nextToken.getDetails().equals("/")){
            lex.putBack(token);
            lex.putBack(nextToken);
            parseTermOperation();
        } else {

        }
    }

    private Node[] parseTermOperation () {


        return new Node[0];
    }

    private Node[] parseFactor() {
        return null;
    }


    // check whether token is correct kind
    private void errorCheck( Token token, String kind ) {
        if( ! token.isKind( kind ) ) {
            System.out.println("Error:  expected " + token + " to be of kind " + kind );
            System.exit(1);
        }
    }

    // check whether token is correct kind and details
    private void errorCheck( Token token, String kind, String details ) {
        if( ! token.isKind( kind ) || ! token.getDetails().equals( details ) ) {
            System.out.println("Error:  expected " + token + " to be kind=" + kind + " and details=" + details );
            System.exit(1);
        }
    }

}
