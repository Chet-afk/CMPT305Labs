
import com.macewan305.PropertyAssessment;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.text.NumberFormat;

import static com.macewan305.CsvPropertyAssessmentDAO.*;
import static com.macewan305.PropertyAssessments.*;
import static com.macewan305.Statistics.*;

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
            System.out.println("There are "+ propertyValues.length + " recorded properties.");  // Returns how many Properties are assessed by counting the total amount of lines

            List<Integer> assessmentList = getAssessmentValues(propertyValues);
            int[] lowestAndHighest = lowHighAssess(assessmentList);

            System.out.println("Highest value is: $" + NumberFormat.getIntegerInstance().format(lowestAndHighest[1]) + "\nLowest value is: $" + NumberFormat.getIntegerInstance().format(lowestAndHighest[0]));
            System.out.println("The range is $" + NumberFormat.getIntegerInstance().format(range(lowestAndHighest)));
            System.out.println("The average assessment value is: $" + NumberFormat.getIntegerInstance().format(mean(assessmentList)));
            System.out.println("The median value is: $" + NumberFormat.getIntegerInstance().format(median(assessmentList)));
            System.out.println();

            // Find a specific Account Number
            System.out.println("Find a property assessment by account number: ");
            String accNum = userInput.nextLine();

            if (!accNum.matches("[0-9]+")){
                System.out.println("This is not a valid account number.");
            } else{
                PropertyAssessment specificAcc = findAccount(propertyValues, Integer.parseInt(accNum));
                if (specificAcc == null){
                    System.out.println("Account number does not exist.");
                } else{
                    System.out.println("Account number = "+ specificAcc.accountNum());
                    System.out.println("Address = " + specificAcc.Address());
                    System.out.println("Assessed value = " + specificAcc.assessmentVal());
                    System.out.println("Assessment Class = "+ specificAcc.AllClasses());
                    System.out.println("Neighbourhood = " + specificAcc.Area());
                    System.out.println("Location = "+ specificAcc.Location());
                }
            }


            // Neighbourhood Filter
            System.out.println("Neighbourhood: ");
            String neighbourhoodName = userInput.nextLine();
            PropertyAssessment[] filteredNeighbourhood = neighbourHoodFilter(propertyValues,neighbourhoodName);
            if (filteredNeighbourhood == null){
                System.out.println("Neighbourhood does not exist");
            } else {

                System.out.println("Descriptive Statistics of Neighbourhood: " + neighbourhoodName);
                System.out.println("There are " + filteredNeighbourhood.length + " recorded properties.");  // Returns how many Properties are assessed by counting the total amount of lines

                List<Integer> filteredAssess = getAssessmentValues(filteredNeighbourhood);
                int[] lowestAndHighestNeighbourhood = lowHighAssess(filteredAssess);

                System.out.println("Highest value is: $" + NumberFormat.getIntegerInstance().format(lowestAndHighestNeighbourhood[1]) + "\nLowest value is: $" + NumberFormat.getIntegerInstance().format(lowestAndHighestNeighbourhood[0]));
                System.out.println("The range is $" + NumberFormat.getIntegerInstance().format(range(lowestAndHighestNeighbourhood)));
                System.out.println("The average assessment value is: $" + NumberFormat.getIntegerInstance().format(mean(filteredAssess)));
                System.out.println("The median value is: $" + NumberFormat.getIntegerInstance().format(median(filteredAssess)));
            }
        }
        catch(Exception e){
            System.out.println("The filename is invalid.");
        }

    }
}