/**
 * Node Class to handle assignment StatementsNode.
 * Implicitly assigns the right most child to the left most child.
 */
public class AssignmentNode extends Node {

    public AssignmentNode (String kind, Node left, Node right, boolean doPrintNode) {
        super(kind, left, right, doPrintNode);
    }

    @Override
    public void executeNode() throws Exception {
        if(variableMemoryMap != null
                && this.first !=  null && this.second != null) {
            String variable = this.first.info;
            double value = new ExpressionNode(second.kind, second, false).executeNodeForReturn();
            variableMemoryMap.put(variable, value);
        }
    }
}
