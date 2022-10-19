/*
This main function implements the CSVDAO and tests their outputs (much like Lab2
 */

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.List;
import java.util.Scanner;

import com.macewan305.CsvPropertyAssessmentDAO;
import com.macewan305.PropertyAssessment;
import com.macewan305.Statistics;

import static com.macewan305.PropertyAssessments.getAssessmentValues;
import static com.macewan305.Statistics.lowHighAssess;
import static com.macewan305.Statistics.range;

public class Lab5MainA {

    public static void main(String[] args) {

        try {

            System.out.println("Enter CSV Name or Absolute Path: ");

            Scanner userInput = new Scanner(System.in);  // Sets up Scanner object that reads from Std Input

            String CSVName = userInput.nextLine(); // Prompt user input
            Path CSVPaths = Paths.get(CSVName);     // Get the path of the CSV and create a Path object from it

            CsvPropertyAssessmentDAO database = new CsvPropertyAssessmentDAO(CSVPaths);

            //System.out.println(database.getAccountNum(1025576));

            PropertyAssessment[] filtered = database.getNeighbourhood("Granville");

            // Descriptive Statistics section for neighbourhood filter
            /*System.out.println("\nDescriptive Statistics (neighbourhood = Granville)");
            System.out.println("There are " + filtered.length + " recorded properties.");  // Returns how many Properties are assessed by counting the total amount of lines

            List<Integer> assessmentList = getAssessmentValues(filtered);
            int[] lowestAndHighest = lowHighAssess(assessmentList);

            System.out.println("Highest value is: $" + NumberFormat.getIntegerInstance().format(lowestAndHighest[1]) + "\nLowest value is: $" + NumberFormat.getIntegerInstance().format(lowestAndHighest[0]));
            System.out.println("The range is $" + NumberFormat.getIntegerInstance().format(range(lowestAndHighest)));
            System.out.println("The average assessment value is: $" + NumberFormat.getIntegerInstance().format(Statistics.mean(assessmentList)));
            System.out.println("The median value is: $" + NumberFormat.getIntegerInstance().format(Statistics.median(assessmentList)));*/


            filtered = database.getAssessClass("Residential");

            // Descriptive Statistics for Class filter
            System.out.println("\nDescriptive Statistics (Assessment Class = Residential)");
            System.out.println("There are " + filtered.length + " recorded properties.");  // Returns how many Properties are assessed by counting the total amount of lines

            List<Integer> assessmentList = getAssessmentValues(filtered);
            int[] lowestAndHighest = lowHighAssess(assessmentList);

            System.out.println("Highest value is: $" + NumberFormat.getIntegerInstance().format(lowestAndHighest[1]) + "\nLowest value is: $" + NumberFormat.getIntegerInstance().format(lowestAndHighest[0]));
            System.out.println("The range is $" + NumberFormat.getIntegerInstance().format(range(lowestAndHighest)));
            System.out.println("The average assessment value is: $" + NumberFormat.getIntegerInstance().format(Statistics.mean(assessmentList)));
            System.out.println("The median value is: $" + NumberFormat.getIntegerInstance().format(Statistics.median(assessmentList)));


        } catch (Exception e) {
            System.out.println("The filename is invalid.");
        }
    }
}
