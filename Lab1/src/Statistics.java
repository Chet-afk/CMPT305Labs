import java.util.List;
import java.io.BufferedReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
            wardCheck(propertyValues);
            assessClass(propertyValues);

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

            current = Integer.parseInt(loadedProperties[index][8]); // Assumes that the property value will always be in the 8th column

            if(index == 0){         // Set lowest count to the first checked value, so there is a baseline for comparison.
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

    public static void wardCheck(String[][] loadedProperties){

        int line = 0;
        List<String> wards = new ArrayList<String>();

        while (line != loadedProperties.length){

            if (wards.contains(loadedProperties[line][7])){     // Checks to see if the ward is part of the list to stop duplicates
                line++;
                continue;
            }
            wards.add(loadedProperties[line][7]);
            line++;
        }

        System.out.println("There are "+wards.size()+" wards.");
    }


    public static void assessClass(String[][] loadedProperties){

        int line = 0;
        List<String> classes = new ArrayList<String>();
        String class1;
        String class2;
        String class3;

        String percent1;
        String percent2;
        String percent3;

        String class1Full;
        String class2Full;
        String class3Full;


        while (line != loadedProperties.length) {

            class1Full = "";
            class2Full = "";
            class3Full = "";

            System.out.println(loadedProperties[line][14]);

            /*if (loadedProperties[line][15].equals(null)) {
                System.out.println("here");
                class1 = loadedProperties[line][15];
                percent1 = loadedProperties[line][12] + "%";

                class1Full = String.join(" ", class1, percent1);

            }

            for(int eachline = 0; eachline < loadedProperties[line].length;eachline++){     // When data was processed in format data, it doesn't count any empty cells past the last one in a row. i.e they don't exist. Any empty cells between however, are empty strings
                System.out.println(loadedProperties[line][eachline]);
            }*/
            classes.add(String.join(", ", class1Full,class2Full,class3Full));

            line++;
        }

        System.out.println(classes);
    }
}
