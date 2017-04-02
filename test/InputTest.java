import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Kendra's Laptop on 3/21/2017.
 */
public class InputTest {

    @Test
    public void executeInputTest() throws Exception {
        Token inputString = new Token("keyword", "x=");
        Token variable = new Token("id", "x");

        InputNode inputNode = new InputNode("input", new Node(inputString), new Node(variable));

        System.out.println("Executing Node");
        inputNode.executeNode();

        Assert.assertTrue(inputNode.getVariableMemoryMap().containsKey("x"));

    }
}
