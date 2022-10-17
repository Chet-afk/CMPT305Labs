/*
This main function implements the CSVDAO
 */

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import com.macewan305.CsvPropertyAssessmentDAO;

public class Lab5MainA {

    public static void main(String[] args) {

        try {

            System.out.println("Enter CSV Name or Absolute Path: ");

            Scanner userInput = new Scanner(System.in);  // Sets up Scanner object that reads from Std Input

            String CSVName = userInput.nextLine(); // Prompt user input
            Path CSVPaths = Paths.get(CSVName);     // Get the path of the CSV and create a Path object from it

            CsvPropertyAssessmentDAO database = new CsvPropertyAssessmentDAO(CSVPaths);

            System.out.println(database.getAccountNum(1025576));

        } catch (Exception e) {
            System.out.println("The filename is invalid.");
        }
    }
}
