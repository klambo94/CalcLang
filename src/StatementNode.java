/**
 * Created by Kendra's Laptop on 3/25/2017.
 */
public class StatementNode extends Node {
    public StatementNode (Token token) {
        super(token);
    }

    public StatementNode (String kind, Node first) {
        super(kind, first);
    }

    public StatementNode (String kind, Node first, Node second) {
        super(kind, first, second);
    }

    public StatementNode (String kind, Node first, Node second, Node third) {
        super(kind, first, second, third);
    }

    @Override
    public void executeNode() throws Exception {
       this.first.executeNode();
    }

}
