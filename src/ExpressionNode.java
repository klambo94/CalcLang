/**
 * Created by Kendra's Laptop on 3/23/2017.
 */
public class ExpressionNode extends Node {

    public ExpressionNode (Token token) {
        super(token);
    }

    public ExpressionNode(String kind, Node one){
        super(kind, one);
    }

    public ExpressionNode(String kind, Node one, Node two) {
        super(kind, one, two);
    }

    public ExpressionNode (String kind, Node one, Node two, Node three) {
        super(kind, one, two, three);
    }

    public void executeNode() throws Exception {

    }

    public double executeNodeForReturn () throws Exception {
        String kind = this.kind;
        Node[] children = this.getChildren();
        if(kind.equals("Expression Node")) {
            if (children.length == 1) {
                return executeTerm(children[0]);
            } else if( children.length == 2) {
              return executeTerm(this);
            } else if(children.length == 3) {
                return executeExpressionOperationStatement();
            } else {
                throw new Exception("Unrecognized Amt of children: " + children.length);
            }
        } else {
            return executeTerm(this);
        }
    }

    private double executeExpressionOperationStatement () throws Exception {
        double operandOne = executeTerm(first);
        String operation = second.info;
        double operandTwo = new ExpressionNode(third.kind, third)
                .executeNodeForReturn();

        switch (operation) {
            case "+":
                return operandOne + operandTwo;
            case "-":
                return operandOne - operandTwo;
            default:
                throw new Exception("Unknown operation: " + operation);
        }
    }

    private double executeTerm (Node node) throws Exception {
        Node[] children = node.getChildren();

        if(children.length == 0) {
            return executeFactor(node);
        } else if(children.length == 1){
            return executeFactor(children[0]);
        } else if(children.length == 2) {
            if(children[0].kind.equals("mathSym")){
                return executeNegativeFactor(children[1]);
            } else if(children[0].kind.equals("bif")) {
                return executeFactor(children);
            } else {
                throw new Exception("Unrecongized kind for node containing 2 children: " +
                        children[0].kind);
            }
        } else if(children.length == 3) {
            return executeTermOperationStatement(children);
        }
        else {
            throw new Exception ("Unrecognized amount of children for a term. Children: " +
                    children.length);
        }
    }

    private double executeNegativeFactor (Node child) throws Exception {

        try{
            return -executeFactor(child);
        } catch (NumberFormatException nfe) {
            throw new Exception("Unable to parse info into a double: " + child.info);
        }
    }



    private double executeTermOperationStatement (Node[] children) throws Exception {
        if(children.length != 3) {
            throw new Exception("Unexpected amount of children:" + children);
        }

        double operandOne = executeTerm(first.first);
        String operation = first.second.info;
        double operandTwo = new ExpressionNode(first.third.kind, first.third)
                .executeNodeForReturn();

        switch (operation) {
            case "*":
                return operandOne * operandTwo;
            case "/":
                return operandOne / operandTwo;
            default:
                throw new Exception("Unknown operation: " + operation);
        }

    }

    private double executeFactor (Node first) throws Exception{
        String kind = first.kind;
        String info = first.info;
        try {
            if (kind.equals("digit")) {
                return Double.parseDouble(info);
            } else if (kind.equals("id")) {
                if (variableMemoryMap != null && variableMemoryMap.containsKey(info)) {
                    return variableMemoryMap.get(info);
                }
            } else {
                throw new Exception("Unrecognized factor with kind: " + kind);
            }
        } catch (NumberFormatException nfe) {
            throw new Exception("Unable to parse digit into a double: " + info);
        }
        throw new Exception("Did not find a match for factor with kind: " + kind);
    }

    private double executeFactor(Node[] children) throws Exception {
       if(children.length != 2){
           throw new Exception("Unexpected amount of children in execute factor");
       }
        String kind = children[0].kind;

        if(kind.equals("bif")) {
            return executeBuiltInFunction(children);
        } else {
            throw new Exception("Unrecognized factor with kind: " + kind);
        }
    }

    private double executeBuiltInFunction (Node[] children) throws Exception {
        if(children.length != 2){
            throw new Exception("Unexpected amount of children in execute built in function");
        }

        String function = children[0].info;
        double expression = new ExpressionNode(children[1].kind, children[1])
                .executeNodeForReturn();
        switch (function) {
            case "sin":
                return Math.sin(expression);
            case "sqrt":
                return Math.sqrt(expression);
            case "cos":
                return Math.cos(expression);
            default:
                throw new Exception("Unrecognized built in function: " + function);
        }
    }


}
