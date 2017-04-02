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

   // public StatementNode (String kind, Node one, Node two) {
     //   super(kind, one, two);
  //  }


    @Override
    public void executeNode() throws Exception {
       this.first.executeNode();
    }

//    @Override
//    public void executeNode() throws Exception {
//        String kind = this.kind;
//
//        switch(kind) {
//            case "id":
//                // When statement node created the first is the first operend and the second is
//                // the second operend. The math symbol does not get passed
//                new AssignmentNode(kind, first, second).executeNode();
//                break;
//            case "keywords":
//                String info = this.info;
//                if(info.equals("input")) {
//                    new InputNode(kind, first, second).executeNode(); //first I believe is the
//                    // input. We just need String V so those would be in the second and third
//                    // children
//                } else if(info.equals("msg") || info.equals("show")) {
//                    new DisplayNode(kind, first).executeNode();
//                } else if(info.equals("newline")){
//                    System.out.println("");
//                } else {
//                    throw new Exception("Unrecognized keyword:" + info);
//                }
//                break;
//            default:
//                throw new Exception("Unrecognized kind: " + kind);
//        }
//    }
}
