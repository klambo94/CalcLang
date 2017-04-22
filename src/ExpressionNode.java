/**
 * Created by Kendra's Laptop on 3/23/2017.
 */
public class ExpressionNode extends Node {

    public ExpressionNode (String kind, Node one){
        super(kind, one);
    }

    public ExpressionNode (String kind, Node one, Node two) {
        super(kind, one, two);
    }

    public ExpressionNode (String kind, Node one, Node two, Node three) {
        super(kind, one, two, three);
    }

    public void executeNode() throws Exception {

    }

    public double executeNodeForReturn() throws Exception {
        String kind = this.kind;
        Node[] children = this.getChildren();

        if(kind.equals("BIF Expression Node")) {
            if(children.length == 1) {
                children = children[0].getChildren();

                if(children[0].kind.equals("Expression Statement Node")) {
                    return executeExpressionStatement(children[0]);
                } else if(children.length == 1) {
                    return executeTerm(children[0]);
                } else {
                    throw new Exception("Unrecognized amount of children for BIF Expression");
                }
            } else {
                throw new Exception("Unrecognized amount of children for BIF expression");
            }
        } else if(kind.contains("Expression")) {

            if (children.length == 1) {
                String childKind = children[0].kind;

                if(childKind.equals("Expression Statement Node")) {
                    return executeExpressionStatement(children[0]);
                } else {
                    return executeTerm(children[0]);
                }
            } else {
                throw new Exception("Unrecognized Amt of children: " + children.length);

            }
        } else if(kind.equals("Term") && children.length == 1){
            return executeTerm(children[0]);
        } else {
            return executeTerm(this);
        }
    }

    private double executeExpressionStatement (Node node) throws Exception {
        Node[] children = node.getChildren();
        if(children.length == 3) {
            double operandOne = executeTerm(children[0]);
            String operation = children[1].info;
            double operandTwo = new ExpressionNode(children[2].kind, children[2])
                    .executeNodeForReturn();

            switch (operation) {
                case "+":
                    return operandOne + operandTwo;
                case "-":
                    return operandOne - operandTwo;
                default:
                    throw new Exception("Unknown operation: " + operation);
            }
        } else {
            throw new Exception("Unexpected amount of children to an expression node: " +
                    children.length);
        }
    }

    private double executeTerm(Node node) throws Exception {
        Node[] children = node.getChildren();

        if(children.length == 0) {
            return executeFactor(node);
        } else if(children.length == 1) {
            return executeFactor(children[0]);
        } else if((children.length == 2
                || children.length == 3)
                && node.first.info.equals("-")){
            if(children.length == 2){
                return -executeFactor(children[1]);
            } else {
                return - -executeFactor(children[2]);
            }

        } else if(children.length == 2
                && node.first.kind.equals("bif")) {
            return executeBuiltInFunction(node);
        } else if(children.length == 3) {

            if(children[0].kind.equals("Expression Statement Node")){
                double operandOne = executeExpressionStatement(children[0]);
                return executeTermStatement(operandOne, children[1], children[2]);
            }
             return executeTermStatement(node);
        }
        else {
            throw new Exception("Unrecognized amount of children for a term. Children: " +
                    children.length);
        }
    }

    private double executeTermStatement (double operandOne, Node second, Node third) throws Exception {
        String operation = second.info;
        double operandTwo = new ExpressionNode(third.kind, third)
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

    private double executeTermStatement (Node node) throws Exception {
        Node[] children = node.getChildren();
        if(children.length != 3) {
            throw new Exception("Unexpected amount of children:" + children);
        }

        double operandOne =  executeTerm(children[0]);
        String operation = children[1].info;
        double operandTwo = new ExpressionNode(children[2].kind, children[2])
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

    private double executeFactor (Node first) throws Exception {
        String kind = first.kind;
        String info = first.info;
        try {

            if(kind.equals("Expression Node")){
                Node[] children = first.getChildren();
                if(children.length == 1) {
                    return executeFactor(children[0]);
                }
            }
            if(kind.equals("mathSym")) {
                Node[] children = first.getChildren();
                if(children.length == 3){

                    return - -executeFactor(children[2]);
                }
            }
            if (kind.equals("digit")) {
                return Double.parseDouble(info);
            } else if (kind.equals("id")) {
                if (variableMemoryMap != null && variableMemoryMap.containsKey(info)) {
                    return variableMemoryMap.get(info);
                } else {
                    throw new Exception("No variable found with id: " + info);
                }
            } else {
                throw new Exception("Unrecognized factor with kind: " + kind);
            }
        } catch (NumberFormatException nfe) {
            throw new Exception("Unable to parse digit into a double: " + info);
        }
    }

    private double executeBuiltInFunction (Node node) throws Exception {
        Node[] children = node.getChildren();

        if(children.length != 2){
            throw new Exception("Unexpected amount of children in execute built in function");
        }

        String function = children[0].info;
        double expression = new ExpressionNode("BIF Expression Node", children[1])
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
