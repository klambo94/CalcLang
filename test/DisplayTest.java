import org.junit.Test;

/**
 * Created by Kendra's Laptop on 3/25/2017.
 */
public class DisplayTest {

    @Test
    public void testDisplayMsg() throws Exception {
        new DisplayNode("msg", new Node(new Token("string", "Display Message Test"))).executeNode();
    }

    @Test
    public void testDisplayShow()  throws Exception {
        Token digitTenToken = new Token("digit", "10");
        Token variableTenToken = new Token("id", "x");
        new AssignmentNode("assignment", new Node(variableTenToken), new Node(digitTenToken)).executeNode();
        new DisplayNode("show", new Node(variableTenToken)).executeNode();
    }
}
