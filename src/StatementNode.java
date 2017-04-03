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

    @Override
    public void executeNode() throws Exception {
       this.first.executeNode();
    }

}
