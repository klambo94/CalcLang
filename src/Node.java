/** a Node holds one node of a parse tree
    with several pointers to children used
    depending on the kind of node
    Modified to fit CalcLand's design but taken from Otter
    Written by Professor Schultz.
*/
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class Node {


    protected int count = 0;
    protected int id;

    protected String kind;
    protected String info;

    protected Node first, second, third;

    /**
     * Memory map of the variables used in the program.
     */
    public static Map<String, Double> variableMemoryMap = new HashMap<>();

    /**
     * Constructs a Node Given it's kind and children
     * @param kind kind
     * @param one Left most child
     * @param two middle child
     * @param three right most child
     */
    public Node( String kind, Node one, Node two, Node three) {
        this.kind = kind;  info = "";
        first = one;  second = two;  third = three;
        id = count;
        count++;
        System.out.println( this );
    }

    public Node(String kind, Node one, Node two) {
        this.kind = kind;  info = "";
        first = one;  second = two;
        id = count;
        count++;
        System.out.println( this );
    }

    public Node(String kind, Node one){
        this.kind = kind;  info = "";
        first = one;
        id = count;
        count++;
        System.out.println( this );
    }

    /**
     * Constructs a Node from a token.
     * Children are null.
     * @param token Token holding its kind and data
     */
    public Node( Token token ) {
        kind = token.getKind();  info = token.getDetails();
        first = null;  second = null;  third = null;
        id = count;
        count++;
        System.out.println( this );
    }

    /**
     * Produces a string representation of a node.
     * Taken from Otter's Node class.
     * @return A String representation of the node.
     */
    public String toString () {
        return "#" + id + "[" + kind + "," + info + "]";
    }

    public abstract void executeNode() throws Exception;

    public Map<String, Double> getVariableMemoryMap() {
        return this.variableMemoryMap;
    }
    /**
     * Produces an array with the non-null children
     * in order. Taken from Otter's Node class
     * @return children
     */
    protected Node[] getChildren () {
        int count = 0;
        if (first != null) count++;
        if (second != null) count++;
        if (third != null) count++;
        Node[] children = new Node[count];
        int k = 0;
        if (first != null) {
            children[k] = first;
            k++;
        }
        if (second != null) {
            children[k] = second;
            k++;
        }
        if (third != null) {
            children[k] = third;
            k++;
        }
        return children;
    }

    //******************************************************
    // graphical display of this node and its subtree
    // in given camera, with specified location (x,y) of this
    // node, and specified distances horizontally and vertically
    // to children
    public void draw( Camera cam, double x, double y, double h, double v ) {

        System.out.println("draw node " + id );

        // set drawing color
        cam.setColor( Color.black );

        String text = kind;
        if( ! info.equals("") ) text += "(" + info + ")";
        cam.drawHorizCenteredText( text, x, y );

        // positioning of children depends on how many
        // in a nice, uniform manner
        Node[] children = getChildren();
        int number = children.length;
        System.out.println("has " + number + " children");

        double top = y - 0.75*v;

        if( number == 0 ) {
            return;
        } else if( number == 1 ) {
            children[0].draw( cam, x, y-v, h/2, v );     cam.drawLine( x, y, x, top );
        } else if( number == 2 ) {
            children[0].draw( cam, x-h/2, y-v, h/2, v );     cam.drawLine( x, y, x-h/2, top );
            children[1].draw( cam, x+h/2, y-v, h/2, v );     cam.drawLine( x, y, x+h/2, top );
        } else if( number == 3 ) {
            children[0].draw(cam, x - h, y - v, h / 2, v);
            cam.drawLine(x, y, x - h, top);
            children[1].draw(cam, x, y - v, h / 2, v);
            cam.drawLine(x, y, x, top);
            children[2].draw(cam, x + h, y - v, h / 2, v);
            cam.drawLine(x, y, x + h, top);
        } else {
            System.out.println("no Node kind has more than 4 children???");
            System.exit(1);
        }

    }// draw


    protected void error( String message ) throws Exception {
        throw new Exception(message);
    }
}