/**
 * Created by Kendra's Laptop on 3/25/2017.
 */
public class StatementsNode extends Node {

    public StatementsNode (String kind, Node first, Node second) {
        super(kind, first, second);
    }

    @Override
    public void executeNode() throws  Exception {
        if(first != null) {
            first.executeNode();
        }
        if(second != null) {
            second.executeNode();
        }
    }


//    @Override
//    public void executeNode() throws Exception {
//        String kind = this.kind;
//
//        switch(kind) {
//            case "id":
//                //first is the variable id, third is the digit.
//                new StatementNode(kind, first, third).executeNode();
//                break;
//            case "keyword":
//                String info = this.info;
//                if(info.equals("input")) {
//                    new StatementNode(kind, second, third).executeNode();
//                } else if(info.equals("msg") || info.equals("show")) {
//                    new StatementNode(kind, second);
//                } else if(info.equals("newline")){
//                    new StatementNode(new Token(kind, info));
//                } else {
//                    throw new Exception("Unrecognized keyword:" + info);
//                }
//                break;
//            default:
//                throw new Exception("Unrecognized kind: " + kind);
//        }
//    }
}
//TODO: Things to keep in mind while creating the parser
//TODO: I've built this in a way that when the statemtntsNode is created you pass in each token.
//TODO: So for the assignmentnode we don't pass in the '=' operator
//TODO: For the (expression) factor we don't pass in the parens. - This needs to be taken care of
// in Parser
//TODO: For the input we only pass in the STRING V in the statement.
//TODO: For MSG or Show we only pass in the STRING or V.