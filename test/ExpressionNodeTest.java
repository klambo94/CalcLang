import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Kendra's Laptop on 3/25/2017.
 */
public class ExpressionNodeTest {
    Token digitTenToken;
    Token digitThreeToken;
    Token variableTenToken;
    Token variableThreeToken;
    Token multiplyToken;
    Token divideToken;
    Token addToken;
    Token subtractToken;

    @Before
    public void beforeTests() throws Exception {
        digitThreeToken = new Token("digit", "3");
        digitTenToken = new Token("digit", "10");
        variableTenToken = new Token("id", "x");
        variableThreeToken = new Token("id", "y");
        multiplyToken = new Token("mathSym", "*");
        divideToken = new Token("mathSym", "/");
        addToken = new Token("mathSym", "+");
        subtractToken = new Token("mathSym", "-");

        new AssignmentNode("assignment", new Node(variableTenToken), new Node(digitTenToken)).executeNode();
        new AssignmentNode("assignment", new Node(variableThreeToken), new Node(digitThreeToken)).executeNode();

    }

    @Test
    public void testExecuteExpressionsForReturnTermThatIsNumber() throws Exception {
        Assert.assertTrue(new ExpressionNode(digitTenToken).executeNodeForReturn() == 10);
        Assert.assertTrue(new ExpressionNode(digitThreeToken).executeNodeForReturn() == 3);
    }

    @Test
    public void testExecuteExpressionsForReturnTermThatIsVariable() throws Exception {
        Assert.assertTrue(new ExpressionNode(variableTenToken).executeNodeForReturn() == 10);
        Assert.assertTrue(new ExpressionNode(variableThreeToken).executeNodeForReturn() == 3);
    }

    @Test
    public void testExecuteExpressionForReturnSimpleExpressionStatement() throws Exception {
        ExpressionNode addExpressionStatement = new ExpressionNode("Expression Node",
                new ExpressionNode(digitThreeToken), new Node(addToken), new ExpressionNode(digitThreeToken));
        ExpressionNode subMultExpressionStatement = new ExpressionNode("Expression Node",
                new ExpressionNode(digitTenToken), new Node(subtractToken), new ExpressionNode(digitThreeToken));

        Assert.assertTrue(addExpressionStatement.executeNodeForReturn() == 6);
        Assert.assertTrue(subMultExpressionStatement.executeNodeForReturn() == 7);
    }

    @Test
    public void testExecuteExpressionForReturnComplexTermExpressionStatement() throws Exception {
        ExpressionNode multipleExpression = new ExpressionNode("Expression Node",
                new ExpressionNode(digitTenToken), new Node(multiplyToken),
                new ExpressionNode(digitThreeToken));
        ExpressionNode divideExpression = new ExpressionNode("Expression Node",
                new ExpressionNode(digitTenToken), new Node(divideToken),
                new ExpressionNode(digitThreeToken));
        ExpressionNode multTerm = new ExpressionNode("Expression Node", multipleExpression);
        ExpressionNode dividTerm = new ExpressionNode("Expression Node", divideExpression);
        Assert.assertTrue(multTerm.executeNodeForReturn() == 30);
        Assert.assertTrue(dividTerm.executeNodeForReturn() == 3.3333333333333335);

    }

    @Test
    public void testExecuteExpressionForReturnComplexExpressionStatement() throws Exception {
        ExpressionNode multipleExpression = new ExpressionNode("Expression Node",
                new ExpressionNode(digitTenToken), new Node(multiplyToken),
                new ExpressionNode(variableThreeToken));
        ExpressionNode divideExpression = new ExpressionNode("Expression Node",
                new ExpressionNode(digitThreeToken), new Node(divideToken),
                new ExpressionNode(variableTenToken));

        ExpressionNode addDivideExpressionStatement = new ExpressionNode("Expression Node",
                new ExpressionNode(variableThreeToken), new Node(addToken), divideExpression);
        ExpressionNode subDivideExpressionStatement = new ExpressionNode("Expression Node",
                new ExpressionNode(variableTenToken), new Node(subtractToken), divideExpression);

        ExpressionNode addMultiExpressionStatement = new ExpressionNode("Expression Node",
                new ExpressionNode(variableThreeToken), new Node(addToken), multipleExpression);
        ExpressionNode subMultiExpressionStatement = new ExpressionNode("Expression Node",
                new ExpressionNode(variableTenToken), new Node(subtractToken), multipleExpression);

        Assert.assertTrue(addDivideExpressionStatement.executeNodeForReturn() == 3.3);
        Assert.assertTrue(subDivideExpressionStatement.executeNodeForReturn() == 9.7);
        Assert.assertTrue(addMultiExpressionStatement.executeNodeForReturn() == 33);
        Assert.assertTrue(subMultiExpressionStatement.executeNodeForReturn() == -20);
    }

    @Test
    public void testExecuteExpressionsForReturnTermThatIsNegativeFactor () throws Exception {
        ExpressionNode negativeTenFactor = new ExpressionNode("Expression Node",
                new Node(subtractToken), new ExpressionNode(variableTenToken));
        ExpressionNode negativeThreeFactor = new ExpressionNode("Expression Node",
                new Node(subtractToken), new ExpressionNode(variableThreeToken));

        Assert.assertTrue(negativeThreeFactor.executeNodeForReturn() == -3);
        Assert.assertTrue(negativeTenFactor.executeNodeForReturn() == -10);
    }

    @Test
    public void testExecuteExpressionsForReturnTermThatIsBIFExpression() throws Exception {
        Token sinBuiltInFunction = new Token("bif", "sin");
        Token cosBuiltInFunction = new Token("bif", "cos");
        Token sqrtBuiltInFunction = new Token("bif", "sqrt");

        ExpressionNode mathThreeFunctionNode = new ExpressionNode("Expression Node",
                new Node(variableThreeToken), new Node(multiplyToken), new Node(variableThreeToken));
        ExpressionNode sinBuiltInFunctionExpression = new ExpressionNode("Expression Node",
                new Node(sinBuiltInFunction), mathThreeFunctionNode);
        ExpressionNode cosBuiltInFunctionExpression = new ExpressionNode("Expression Node",
                new Node(cosBuiltInFunction), mathThreeFunctionNode);
        ExpressionNode sqrtBuiltInFunctionExpression = new ExpressionNode( "ExpressionNode",
                new Node(sqrtBuiltInFunction), mathThreeFunctionNode);


        Assert.assertTrue(sinBuiltInFunctionExpression.executeNodeForReturn() == 0.4121184852417566);
        Assert.assertTrue(cosBuiltInFunctionExpression.executeNodeForReturn() == -0.9111302618846769);
        Assert.assertTrue(sqrtBuiltInFunctionExpression.executeNodeForReturn() == 3);
    }

}
