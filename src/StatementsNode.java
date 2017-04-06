/**
 * Created by Kendra's Laptop on 3/25/2017.
 */
public class StatementsNode extends Node {

    public StatementsNode (String kind, Node first, boolean doPrintNode) {super(kind, first, doPrintNode);}
    public StatementsNode (String kind, Node first, Node second, boolean doPrintNode) {
        super(kind, first, second, doPrintNode);
    }

    @Override
    public void executeNode () throws Exception {
        if (first != null) {
            first.executeNode();
        }
        if (second != null) {
            second.executeNode();
        }
    }
}