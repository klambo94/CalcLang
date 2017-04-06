/**
 * Created by Kendra's Laptop on 3/25/2017.
 */
public class StatementNode extends Node {
    public StatementNode (Token token, boolean doPrintNode) {
        super(token, doPrintNode);
    }

    public StatementNode (String kind, Node first, boolean doPrintNode) {
        super(kind, first, doPrintNode);
    }

    public StatementNode (String kind, Node first, Node second, boolean doPrintNode) {
        super(kind, first, second, doPrintNode);
    }

    public StatementNode (String kind, Node first, Node second, Node third, boolean doPrintNode) {
        super(kind, first, second, third, doPrintNode);
    }

    @Override
    public void executeNode() throws Exception {
       this.first.executeNode();
    }

}
