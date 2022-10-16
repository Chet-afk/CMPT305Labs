import com.macewan305.PropertyAssessment;
import com.macewan305.Statistics;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.List;
import java.util.Scanner;

import static com.macewan305.PropertyAssessments.*;
import static com.macewan305.Statistics.*;

public class Lab3Main {
    public static void main(String[] args) {

        try {

            System.out.println("Enter CSV Name or Absolute Path: ");

            Scanner userInput = new Scanner(System.in);  // Sets up Scanner object that reads from Std Input

            String CSVName = userInput.nextLine(); // Prompt user input
            Path CSVPaths = Paths.get(CSVName);     // Get the path of the CSV and create a Path object from it

            System.out.println("The File Path is " + CSVPaths.toAbsolutePath());

            PropertyAssessment[] propertyValues = formatData(CSVPaths);

            System.out.println("Enter Assessment Class: ");
            String assessClass = userInput.nextLine();

            PropertyAssessment[] filteredProperties = assessClassFilter(propertyValues, assessClass);

            if (filteredProperties == null){
                System.out.println("There are no properties with this assessment class");
            } else {
                // Descriptive Statistics section
                System.out.println("Descriptive Statistics (assessment class = " + assessClass + ")");
                System.out.println("There are " + filteredProperties.length + " recorded properties.");  // Returns how many Properties are assessed by counting the total amount of lines

                List<Integer> assessmentList = getAssessmentValues(filteredProperties);
                int[] lowestAndHighest = lowHighAssess(assessmentList);

                System.out.println("Highest value is: $" + NumberFormat.getIntegerInstance().format(lowestAndHighest[1]) + "\nLowest value is: $" + NumberFormat.getIntegerInstance().format(lowestAndHighest[0]));
                System.out.println("The range is $" + NumberFormat.getIntegerInstance().format(range(lowestAndHighest)));
                System.out.println("The average assessment value is: $" + NumberFormat.getIntegerInstance().format(mean(assessmentList)));
                System.out.println("The median value is: $" + NumberFormat.getIntegerInstance().format(Statistics.median(assessmentList)));
            }

        } catch (Exception e) {
            System.out.println("The filename is invalid.");

        }
    }
}