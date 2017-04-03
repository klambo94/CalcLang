import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by Kendra's Laptop on 3/21/2017.
 */
public class AssignmentTest {

    @Test
    public void testExecuteAssignment () throws Exception {
        Token variableToken = new Token("id", "x");
        Token valueToken = new Token("digit", "3");
        AssignmentNode assignmentNode = new AssignmentNode("",
                new StatementNode(variableToken), new StatementNode(valueToken));
        assignmentNode.executeNode();
        Map<String, Double> varMap = assignmentNode.getVariableMemoryMap();

        Assert.assertTrue(varMap.containsKey("x"));

        Double value = varMap.get("x");
        Assert.assertTrue(3.0 == value);
    }

}
