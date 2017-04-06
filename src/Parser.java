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
        try{
            if(!token.isKind("eof")){
                lex.putBack(token);
                first = parseStatement();
                second = parseStatements();

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if(first != null && second == null) {
            return new StatementsNode("Statements", first, true);
        } else if(first != null && second != null) {
            return new StatementsNode("Statements", first, second, true);
        } else {
            return null;
        }
    }

    public Node parseStatement() throws Exception {
        System.out.println("----------------------> Parsing Statement");
        Node first = null;
        Token token = lex.getToken();

        if(token.isKind("id")) {
            lex.putBack(token);
           first = createAssignmentNode();
        } else if(token.isKind("keyword")) {
            lex.putBack(token);
            first = createKeyWordNode();
        } else if(token.getDetails().equals(")")){
            System.out.println("consuming unneeded symbol: " + token.getDetails());
        }

        return new StatementNode("Statement" , first, true);

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
                return new DisplayNode("msg", new StatementNode(token, true), true);
            } else if(tokenDetails.equals("show")) {
                Node first = parseExpression();
                return new DisplayNode("show", first, true);
            } else {
                return  new DisplayNode("newline", new StatementNode(token, true), true);
            }

        } else if(tokenDetails.equals("input")){
            Token displayString = lex.getToken();
            Token inputVar = lex.getToken();

            return  new InputNode("Input", new StatementNode(displayString, true), new
                    StatementNode(inputVar, true), true);
        } else {
            throw new Exception("Reached an unknown keyword: " + tokenDetails);
        }
    }

    private Node createAssignmentNode () throws Exception {
        System.out.println("----------------------> Creating Assignment Node");
        Token token = lex.getToken();
        String token2Details = lex.getToken().getDetails();
        if(token2Details.equals("=")){
          Node first = new StatementNode(token, true);
          Node second = parseExpression();

            return new AssignmentNode("Assignment", first, second, true);
        } else {
            throw new Exception("Unknown id case: " + token2Details);
        }
    }

    private Node parseExpression()  throws Exception {
        System.out.println("----------------------> Parsing Expression");
        Token token = lex.getToken();
        Node first = null; Node second = null; Node third = null;
        lex.putBack(token);
        Node[] terms  = parseTerm();
        String kind = "";

        Token nextToken = lex.getToken();
        if(nextToken.getDetails().equals("+")
                || nextToken.getDetails().equals("-")){
            kind = "Expression Statement Node";
            first = turnNodeArrayIntoNode(terms, "Term");
            second = new StatementNode(nextToken, true);
            third = parseExpression();
        } else {
            kind = "Expression Node";
            lex.putBack(nextToken);
            if(terms.length >= 1){
                first = terms[0];
            }

            if(terms.length >= 2) {
                second = terms[1];
            }

            if(terms.length >= 3) {
                third = terms[2];
            }
        }



        if(first != null && second == null && third == null){
            return new ExpressionNode(kind, first, true);
        } else if(first != null && second != null && third == null) {
            return new ExpressionNode(kind, first, second, true);
        } else if(first != null && second != null && third != null){
            return new ExpressionNode(kind, first, second, third, true);
        } else {
            throw new Exception("Unexpected amount of nodes: " + terms.length);
        }
    }

    private Node[] parseTerm() throws Exception {

        Token token = lex.getToken();
        Token nextToken = lex.getToken();
        if(nextToken.getDetails().equals("*")
                || nextToken.getDetails().equals("/")){
            Node second= null;
            lex.putBack(nextToken);
            lex.putBack(token);
            Node first = turnNodeArrayIntoNode(parseFactor(), "Term");
            second = new StatementNode(lex.getToken(),true );
            Node third = turnNodeArrayIntoNode(parseTerm(), "Term");

            return new Node[] {first, second, third};
        } else {
            lex.putBack(nextToken);
            lex.putBack(token);
            return parseFactor();
        }
    }

    private Node turnNodeArrayIntoNode (Node[] nodes, String kind) throws Exception {
        Node first= null; Node second = null; Node third = null;

        if(nodes.length >= 1) {
            first = nodes[0];
        }

        if(nodes.length >= 2) {
            second = nodes[1];
        }

        if(nodes.length >= 3) {
            third = nodes[2];
        }
        if(first != null && second == null){
            return first;
        } else if(first != null && second != null && third == null) {
            return new StatementNode(kind, first, second, true);
        } else if(first != null && second != null && third != null) {
            return new StatementNode(kind, first, second, third, true);
        } else {
            throw new Exception("Unexpected amount of nodes in array:" + nodes.length);
        }

    }


    private Node[] parseFactor() throws Exception {
        Token token = lex.getToken();
        Node first = null;
        Node second = null;
        if(token.isKind("id")
                || token.isKind("digit")) {
            first = new StatementNode(token, true);
        } else if(token.getDetails().equals("-")) {
            first = new StatementNode(token, true);
            Node[] nodes = parseFactor();
            if(nodes.length >= 1){
                second = nodes[0];
            }
        } else if(token.getDetails().equals("(")){
            first = parseExpression();
            token = lex.getToken();
            if(!token.getDetails().equals(")")){
                throw new Exception("Consumed a token that was not ')' " + token.getKind() +
                        token.getDetails());
            }
        } else if(token.isKind("bif")) {
            first = new StatementNode(token, true);
            second = parseExpression();
        }

        if(first != null && second == null) {
            return new Node[] {first};
        } else if(first != null && second != null) {
            return new Node[] {first, second};
        } else {
            throw new Exception("First and second node factors are null.");
        }

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
