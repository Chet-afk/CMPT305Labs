package com.macewan305;

import java.util.List;
import java.io.BufferedReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.file.Files;

public class PropertyAssessments {


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

    public static int numOfLines(String[][] loadedProperties){
        int lineCount = 0;
        while (lineCount != loadedProperties.length) { // Go through all lines and add 1 to the count until its at the last line
            lineCount++;
        }
        return lineCount;
    }

    public static int[] lowHighAssess(String[][] loadedProperties){
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

        int[] returnVals = new int[2];
        returnVals[0] = lowAssess;
        returnVals[1] = highAssess;

        return returnVals;

    }

    public static int range(int[] lowHigh){
        int rangeVal = lowHigh[1] - lowHigh[0];
        return rangeVal;
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


    public static List<String> assessClass(String[][] loadedProperties){

        int line = 0;
        List<String> classes = new ArrayList<>();


        while (line != loadedProperties.length) {

            String class1Full;
            String class2Full;
            String class3Full;


            if (loadedProperties[line].length == 16) {      // 16 means that there is only 1 assessment class. 17 / 18 for 2 and 3 classes respectively [Do not want to go out of bounds]

                class1Full = String.join(" ",loadedProperties[line][15], loadedProperties[line][12] + "%");
                classes.add(class1Full);

            }

            else if (loadedProperties[line].length == 17) {

                class1Full = String.join(" ",loadedProperties[line][15], loadedProperties[line][12] + "%");
                class2Full = String.join(" ",loadedProperties[line][16], loadedProperties[line][13] + "%");

                classes.add(String.join(" / ", class1Full,class2Full));

            }

            else if (loadedProperties[line].length == 18) {

                class1Full = String.join(" ",loadedProperties[line][15], loadedProperties[line][12] + "%");
                class2Full = String.join(" ",loadedProperties[line][16], loadedProperties[line][13] + "%");
                class3Full = String.join(" ",loadedProperties[line][17], loadedProperties[line][14] + "%");

                classes.add(String.join(" / ", class1Full,class2Full,class3Full));

            }


            line++;
        }

        return classes;
    }
}
