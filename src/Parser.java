/*
    This class provides a recursive descent parser of CalcLang,
    creating a parse tree. Taken from Otter's Parser which is created by
    Schultz.
*/


import java.util.concurrent.ExecutionException;

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
            lex.putBack(token);
            first = createKeyWordNode();
        } else if(token.getDetails().equals(")")){
            System.out.println("consuming uneeded symbol: " + token.getDetails());
        }

        return new StatementNode("Statement" , first);

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

    private Node parseExpression () throws Exception {
        System.out.println("----------------------> Parsing Expression");
        Token token = lex.getToken();
        Node first = null;
        Node second = null;
        Node third = null;
        Node[] children;

        Token nextToken = lex.getToken();
        if(nextToken.getDetails().equals("+")||
                nextToken.getDetails().equals("-")) {
            lex.putBack(nextToken);
            lex.putBack(token);

            children = parseExpressionOperation();
        } else {
            lex.putBack(nextToken);
            lex.putBack(token);
            children = parseTerm();
        }

        if(children.length >= 1) {
            first = children[0];
        }

        if(children.length >= 2) {
            second = children[1];
        }

        if(children.length == 3) {
            third = children[2];
        }
        if(first != null && (second == null && third == null)){
            return new ExpressionNode("Expression Node", first);
        } else if(first != null && second != null && third == null) {
            return new ExpressionNode("Expression Node", first, second);
        } else {
            return new ExpressionNode("Expression Node", first, second, third);
        }

    }

    private Node[] parseExpressionOperation() throws Exception {
        Node first= parseTerm()[0];
        Token secondToken = lex.getToken();
        Node second = new StatementNode(secondToken);
        Node third = parseExpression();

        return new Node[] {first, second, third};
    }

    private Node[] parseTerm() throws Exception {
        Token token = lex.getToken();
        Token nextToken = lex.getToken();
        Node[] children;
        if(nextToken.getDetails().equals("*")
                || nextToken.getDetails().equals("/")){
            lex.putBack(nextToken);
            lex.putBack(token);
           children = parseTermOperation();
        } else {
            lex.putBack(nextToken);
            lex.putBack(token);
            children = parseFactor();
        }
        return children;
    }

    private Node[] parseTermOperation () throws Exception {
        Node first = parseFactor()[0];
        Node second = new StatementNode(lex.getToken());
        Node[] thirdChildren = parseTerm();

        Node third = null;
        if(thirdChildren.length == 2) {
            third = thirdChildren[0];
        } else{
            throw new Exception("Amt of node children expected was different: " + thirdChildren.length);
        }

        return new Node[] { first, second, third};
    }

    private Node[] parseFactor() throws Exception {
        Token token = lex.getToken();
        Node first = null;
        Node second = null;
        if(token.isKind("digit")
                || token.isKind("id")) {
            first = new StatementNode(token);
        } else if(token.getDetails().equals("(")){ //TODO: I think this is creating the extra
            // expression node
            first = parseExpression();
        } else if(token.getDetails().equals("-")) {
            first = new StatementNode(token);
            second = parseFactor()[0];
        } else if(token.isKind("bif")) {
            first = new StatementNode(token);
            second = parseExpression();
        } else {
            throw new Exception("Unrecongized factor: " + token.getKind() + " " + token
                    .getDetails());
        }

        return new Node[] {first, second};
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
