/**
 * Created by Kendra's Laptop on 3/25/2017.
 */
public class StatementsNode extends Node {

    public StatementsNode (String kind, Node first, Node second) {
        super(kind, first, second);
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