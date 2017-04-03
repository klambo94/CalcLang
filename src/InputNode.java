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


        double inputValue = getUserInput(outputText);


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

    private double getUserInput(String outputText)  throws  Exception {
        Scanner keys = new Scanner(System.in);
        boolean badInfoEntered = true;
        double returnValue = -1;

        while(badInfoEntered) {
            System.out.print(outputText);
            String input = keys.nextLine();
            if(isNumeric(input)){
                returnValue = Double.parseDouble(input);
                badInfoEntered = false;
            }
        }
        return returnValue;
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number");
            return false;
        }
        return true;
    }
}
