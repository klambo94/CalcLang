/**
 * Node class for Displaying information to the screen.
 * This class works with build in functions "msg" and "show v"
 */
public class DisplayNode extends Node {

    public DisplayNode (String kind, Node one, boolean doPrintNode) {
        super(kind, one, doPrintNode);
    }

    @Override
    public void executeNode()throws Exception {
        String firstKind = (first != null) ? first.kind : null;
        String displayInfo = null;

        if(!firstKind.equals("Expression Node")) {
            String firstInfo = first.info;
            if (firstKind.equals("string")) { //This is case for keyword message
                displayInfo = firstInfo.replaceAll("\"", "");
            }  else if (kind.equals("newline")) {
                displayInfo = "\n";
            } else {
                error("The child is null. Nothing to display");
            }
        } else {
            if (kind.equals("show")) {
                displayInfo = String.valueOf(new ExpressionNode(first.kind, first, false)
                        .executeNodeForReturn());
            }
        }

        System.out.print(displayInfo);
    }
}
