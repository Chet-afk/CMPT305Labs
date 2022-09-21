import java.io.BufferedReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.nio.file.Files;

public class Statistics {
    public static void main(String[] args){

        try {

            System.out.println("Enter CSV Name or Absolute Path: ");

            Scanner inputFilePath = new Scanner(System.in);  // Sets up Scanner object that reads from Std Input

            String CSVName = inputFilePath.nextLine(); // Prompt user input
            Path CSVPaths = Paths.get(CSVName);     // Get the path of the CSV and create a Path object from it

            System.out.println("The File Path is " + CSVPaths.toAbsolutePath());

            String[][] propertyValues = formatData(CSVPaths);

            numOfLines(propertyValues);  // Returns how many Properties are assessed by counting the total amount of lines
            lowHighAssess(propertyValues);
        }
        catch(Exception e){
            System.out.println("broken");
        }

    }

    public static String[][] formatData(Path CSVFile) throws Exception{
        BufferedReader lineBuffer = Files.newBufferedReader(CSVFile);
        lineBuffer.readLine(); // Remove headers
        String eachLine;

        int index = 0;
        String[][] data = new String[100][];
        while((eachLine = lineBuffer.readLine()) != null){
            String[] propertyData = eachLine.split(",");

            if(index == data.length){
                data = Arrays.copyOf(data, data.length * 2);
            }

            data[index] = propertyData;
            index++;
        }

        return Arrays.copyOf(data, index);
    }

    public static void numOfLines(String[][] loadedProperties){
        int lineCount = 0;
        while (lineCount != loadedProperties.length) { // Go through all lines and add 1 to the count until its at the last line
            lineCount++;
        }
        System.out.println("There are "+ lineCount  + " recorded properties.");
    }

    public static void lowHighAssess(String[][] loadedProperties){
        int lowAssess = 0;
        int highAssess = 0;
        int current;
        int index = 0;

        while(index != loadedProperties.length){

            current = Integer.parseInt(loadedProperties[index][8]); // Assumes that the property value will always be in the

            if(index == 0){
                lowAssess = current;
            }
            if (current > highAssess) {
                highAssess = current;
            } else if (current < lowAssess) {
                lowAssess = current;
            }
            index++;

    }

        System.out.println("Highest value is: $" + highAssess + "\nLowest value is: $" + lowAssess);

    }
}
