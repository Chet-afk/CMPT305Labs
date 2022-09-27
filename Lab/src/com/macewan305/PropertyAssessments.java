package com.macewan305;

import java.util.List;
import java.io.BufferedReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.file.Files;
import static java.lang.Math.round;

public class PropertyAssessments {


    public static PropertyAssessment[] formatData(Path CSVFile) throws Exception{
        BufferedReader lineBuffer = Files.newBufferedReader(CSVFile);
        lineBuffer.readLine(); // Remove headers
        String eachLine;

        int index = 0;
        PropertyAssessment[] data = new PropertyAssessment[100];

        while((eachLine = lineBuffer.readLine()) != null){
            String[] propertyData = eachLine.split(",");
            PropertyAssessment eachProperty = new PropertyAssessment();

            eachProperty.accountNum = Integer.parseInt(propertyData[0]);
            eachProperty.suite = propertyData[1];
            eachProperty.houseNum = propertyData[2];
            eachProperty.streetName = propertyData[3];
            eachProperty.garage=propertyData[4];
            eachProperty.neighID = propertyData[5];
            eachProperty.neighName =propertyData[6];
            eachProperty.ward= propertyData[7];
            eachProperty.assessment = Integer.parseInt(propertyData[8]);
            eachProperty.lat = propertyData[9];
            eachProperty.lon = propertyData[10];
            eachProperty.point = propertyData[11];
            eachProperty.assess1P = Integer.parseInt(propertyData[12]);
            eachProperty.assess2P = Integer.parseInt("0" + propertyData[13]); // "0" allows empty strings to be parsed to 0;
            eachProperty.assess3P = Integer.parseInt("0" + propertyData[14]);
            eachProperty.assess1Name = propertyData[15];

            if (propertyData.length == 17) {        // This section lets us check to see if property data has more assessment names. If it didn't then it would go oob
                eachProperty.assess2Name = propertyData[16];
            } else if (propertyData.length == 18) {
                eachProperty.assess2Name = propertyData[16];
                eachProperty.assess3Name = propertyData[17];
            }
            if(index == data.length){

                data = Arrays.copyOf(data, data.length * 2);
            }

            data[index] = eachProperty;
            index++;

        }

        return Arrays.copyOf(data, index);
    }

    public static int numOfLines(PropertyAssessment[] loadedProperties){

        return loadedProperties.length;
    }

    public static int[] lowHighAssess(PropertyAssessment[] loadedProperties){
        int lowAssess = 0;
        int highAssess = 0;
        int current;
        int index = 0;

        while(index != loadedProperties.length){

            current = loadedProperties[index].assessment; // Assumes that the property value will always be in the 8th column

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

    public static int mean(PropertyAssessment[] loadedProperties){
        int line = 0;
        long total = 0; // long because total value gets too big for regular ints
        while (line != loadedProperties.length){
            total += loadedProperties[line].assessment;
            line++;
        }
        return (Math.round(total / loadedProperties.length));
    }

    public static void wardCheck(PropertyAssessment[] loadedProperties){

        int line = 0;
        List<String> wards = new ArrayList<String>();

        while (line != loadedProperties.length){

            if (wards.contains(loadedProperties[line].ward)){     // Checks to see if the ward is part of the list to stop duplicates
                line++;
                continue;
            }
            wards.add(loadedProperties[line].ward);
            line++;
        }

        System.out.println("There are "+wards.size()+" wards.");
    }


    /*public static List<String> assessClass(PropertyAssessment[] loadedProperties){

        int line = 0;
        List<String> classes = new ArrayList<>();


        while (line != loadedProperties.length) {

            String class1Full;
            String class2Full;
            String class3Full;


            if (loadedProperties.length == 16) {      // 16 means that there is only 1 assessment class. 17 / 18 for 2 and 3 classes respectively [Do not want to go out of bounds]

                class1Full = String.join(" ",loadedProperties[line][15], loadedProperties[line][12] + "%");
                classes.add(class1Full);

            }

            else if (loadedProperties.length == 17) {

                class1Full = String.join(" ",loadedProperties[line][15], loadedProperties[line][12] + "%");
                class2Full = String.join(" ",loadedProperties[line][16], loadedProperties[line][13] + "%");

                classes.add(String.join(" / ", class1Full,class2Full));

            }

            else if (loadedProperties.length == 18) {

                class1Full = String.join(" ",loadedProperties[line][15], loadedProperties[line][12] + "%");
                class2Full = String.join(" ",loadedProperties[line][16], loadedProperties[line][13] + "%");
                class3Full = String.join(" ",loadedProperties[line][17], loadedProperties[line][14] + "%");

                classes.add(String.join(" / ", class1Full,class2Full,class3Full));

            }


            line++;
        }

        return classes;
    }*/
}
