/**
 * Node class for Displaying information to the screen.
 * This class works with build in functions "msg" and "show v"
 */
public class DisplayNode extends Node {

    public DisplayNode (String kind, Node one) {
        super(kind, one);
    }

    @Override
    public void executeNode()throws Exception {
        String firstKind = (first != null) ? first.kind : null;
        String displayInfo = null;

        if(!firstKind.equals("Expression Node")
            && !firstKind.equals("Expression Statement Node")) {
            String firstInfo = first.info;
            if (firstKind.equals("string")) { //This is case for keyword message
                displayInfo = firstInfo.replaceAll("\"", "");
            }  else if (kind.equals("newline")) {
                displayInfo = "\n";
            } else {
                error("Unrecongized node");
            }
        } else {
            if (kind.equals("show")) {
                displayInfo = String.valueOf(new ExpressionNode(first.kind, first)
                        .executeNodeForReturn());
            }
        }

        System.out.print(displayInfo);
    }
}
