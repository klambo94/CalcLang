import java.util.Scanner;

/**
 * This class works with dispalying information to the screen and
 * receiving input from the user. It will display "input 'a ='" and wait for input.
 * Once input has been received it will store the input into the variable a.
 */
public class InputNode extends Node{


    public InputNode (String kind, Node one, Node two) {
        super(kind, one, two);
    }

    @Override
    public void executeNode() throws  Exception {
        String outputText = getFirstChildInfo();
        String variable = getSecondChildInfo();

        System.out.print(outputText);
        double inputValue = getUserInput();


        if (variableMemoryMap != null) {
            if((variable != null && !variable.isEmpty())) {
                variableMemoryMap.put(variable, inputValue);
            } else {
                error("Variable is null or empty");
            }
        }
    }

    private String getFirstChildInfo () throws  Exception {
        String outputText = "";
        if (first != null) {
            outputText = first.info;
        } else {
            error("First child is null. Nothing to display");
        }
        return outputText;
    }

    private String getSecondChildInfo() throws Exception {
        String infoText = "";
        if (this.second != null) {
            infoText = this.second.info;
        } else {
            error("Second child is null, cannot determine which variable to save answer in.");
        }
        return infoText;
    }

    private double getUserInput()  throws  Exception {
        Scanner keys = new Scanner(System.in);
        boolean badInfoEntered = true;
        double returnValue = -1;
        while(badInfoEntered) {
            try {
                returnValue = keys.nextDouble();
                badInfoEntered = false;
            } catch(Exception e) {
                System.out.print("Not a number. Please enter a digit");
            }
        }
        return returnValue;
    }
}
