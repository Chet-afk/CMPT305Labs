
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

            Scanner inputFilePath = new Scanner(System.in);  // Sets up Scanner object that reads from Std Input

            String CSVName = inputFilePath.nextLine(); // Prompt user input
            Path CSVPaths = Paths.get(CSVName);     // Get the path of the CSV and create a Path object from it

            System.out.println("The File Path is " + CSVPaths.toAbsolutePath());

            String[][] propertyValues = formatData(CSVPaths);

            // Function calls section
            System.out.println("Descriptive Statistics of all property Assessments");
            System.out.println("There are "+ numOfLines(propertyValues) + " recorded properties.");  // Returns how many Properties are assessed by counting the total amount of lines

            int[] lowestAndHighest = lowHighAssess(propertyValues);

            System.out.println("Highest value is: $" + NumberFormat.getIntegerInstance().format(lowestAndHighest[1]) + "\nLowest value is: $" + NumberFormat.getIntegerInstance().format(lowestAndHighest[0]));
            System.out.println("The range is $" + NumberFormat.getIntegerInstance().format(range(lowestAndHighest)));
            wardCheck(propertyValues);
            assessClass(propertyValues);


        }
        catch(Exception e){
            System.out.println("The filename is invalid.");
        }

    }
}
