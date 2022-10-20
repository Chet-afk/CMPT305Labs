/*
This main function implements the API DAO
 */

import com.macewan305.*;


import java.text.NumberFormat;
import java.util.*;

import static com.macewan305.PropertyAssessments.getAssessmentValues;
import static com.macewan305.Statistics.lowHighAssess;
import static com.macewan305.Statistics.range;

public class Lab5MainB {

    public static void main(String[] args) {

        System.out.println("Enter account number: ");
        Scanner sysin = new Scanner(System.in);

        int accnum = sysin.nextInt();

        ApiPropertyAssessmentDAO database = new ApiPropertyAssessmentDAO();

        System.out.println(database.getAccountNum(accnum));


        List<PropertyAssessment> filtered = database.getData(10, 20);
        // Descriptive Statistics section for neighbourhood filter
        System.out.println("\nDescriptive Statistics (neighbourhood = Granville)");
        System.out.println("There are " + filtered.size() + " recorded properties.");  // Returns how many Properties are assessed by counting the total amount of lines

        List<Integer> assessmentList = getAssessmentValues(filtered);
        int[] lowestAndHighest = lowHighAssess(assessmentList);

        System.out.println("Highest value is: $" + NumberFormat.getIntegerInstance().format(lowestAndHighest[1]) + "\nLowest value is: $" + NumberFormat.getIntegerInstance().format(lowestAndHighest[0]));
        System.out.println("The range is $" + NumberFormat.getIntegerInstance().format(range(lowestAndHighest)));
        System.out.println("The average assessment value is: $" + NumberFormat.getIntegerInstance().format(Statistics.mean(assessmentList)));
        System.out.println("The median value is: $" + NumberFormat.getIntegerInstance().format(Statistics.median(assessmentList)));

    }
}
