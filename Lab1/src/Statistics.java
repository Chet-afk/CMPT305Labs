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

    public static void wardCheck(String[][] loadedProperties){

        int line = 0;
        List<String> wards = new ArrayList<String>();

        while (line != loadedProperties.length){

            if (wards.contains(loadedProperties[line][7])){
                line++;
                continue;
            }
            wards.add(loadedProperties[line][7]);
            line++;
        }

        System.out.println(wards.size());
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

        while (line != loadedProperties.length) {

            String class1Full = "";
            String class2Full = "";
            String class3Full = "";



            if (loadedProperties[line][15] != "") {

                class1 = loadedProperties[line][15];
                percent1 = loadedProperties[line][12] + "%";

                class1Full = String.join(" ", class1, percent1);

            }

            if (loadedProperties[line][16] != "") {

                class2 = loadedProperties[line][16];
                percent2 = loadedProperties[line][13] + "%";

                class2Full = String.join(" ", class2, percent2);
            }

            if (loadedProperties[line][17] != "") {
                class3 = loadedProperties[line][17];
                percent3 = loadedProperties[line][14] + "%";

                class3Full = String.join(" ", class3, percent3);
            }
            classes.add(String.join(", ", class1Full,class2Full,class3Full));

            line++;
        }

        System.out.println(classes);
    }
}
