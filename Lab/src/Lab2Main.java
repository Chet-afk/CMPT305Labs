
import com.macewan305.PropertyAssessment;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.text.NumberFormat;

import static com.macewan305.PropertyAssessments.*;
import static com.macewan305.PropertyAssessment.*;

public class Lab2Main {
    public static void main(String[] args){

        try {

            System.out.println("Enter CSV Name or Absolute Path: ");

            Scanner userInput = new Scanner(System.in);  // Sets up Scanner object that reads from Std Input

            String CSVName = userInput.nextLine(); // Prompt user input
            Path CSVPaths = Paths.get(CSVName);     // Get the path of the CSV and create a Path object from it

            System.out.println("The File Path is " + CSVPaths.toAbsolutePath());

            PropertyAssessment[] propertyValues = formatData(CSVPaths);

            // Descriptive Statistics sections for all properties
            System.out.println("Descriptive Statistics of all property Assessments");
            System.out.println("There are "+ numOfLines(propertyValues) + " recorded properties.");  // Returns how many Properties are assessed by counting the total amount of lines

            int[] lowestAndHighest = lowHighAssess(propertyValues);

            System.out.println("Highest value is: $" + NumberFormat.getIntegerInstance().format(lowestAndHighest[1]) + "\nLowest value is: $" + NumberFormat.getIntegerInstance().format(lowestAndHighest[0]));
            System.out.println("The range is $" + NumberFormat.getIntegerInstance().format(range(lowestAndHighest)));
            System.out.println("The average assessment value is: $" + NumberFormat.getIntegerInstance().format(mean(propertyValues)));
            System.out.println("The median value is: $" + NumberFormat.getIntegerInstance().format(median(propertyValues)));
            System.out.println();
            //assessClass(propertyValues);

            // Find a specific Account Number
            System.out.println("Find a property assessment by account number: ");
            String accNum = userInput.nextLine();

            //if accNum.matches("[0-9]+"){

            //}
        }
        catch(Exception e){
            System.out.println("The filename is invalid.");
        }

    }
}