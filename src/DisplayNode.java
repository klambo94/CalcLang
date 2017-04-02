/**
 * Node class for Displaying information to the screen.
 * This class works with build in functions "msg" and "show v"
 */
public class DisplayNode extends Node {

    public DisplayNode(String kind, Node one) {
        super(kind, one);
    }
    public DisplayNode(Token token) {super(token);}

    @Override
    public void executeNode()throws Exception {
        String firstInfo = (first != null) ? first.info : null;
        String displayInfo = null;

        if((firstInfo != null && !firstInfo.isEmpty())
                && (kind != null && !kind.isEmpty())) {

            if (kind.equals("string")) { //This is case for keyword message
                displayInfo = firstInfo.replaceAll("\"", "");

            } else if (kind.equals("show")) {
                displayInfo = String.valueOf(new ExpressionNode("Expression", first)
                        .executeNodeForReturn());
            } else if (kind.equals("newline")) {
                displayInfo = "\n";
            }else {
               error("Reached an unknown display node: "  + kind + "With info " + info);
            }

        } else {
            error("The child is null. Nothing to display");
        }

        System.out.print(displayInfo);
    }
}
